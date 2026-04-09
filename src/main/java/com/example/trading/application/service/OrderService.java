package com.example.trading.application.service;

import com.example.trading.application.port.output.MarketDataPublisherPort;
import com.example.trading.application.port.output.OrderRepositoryPort;
import com.example.trading.application.port.output.TradeRepositoryPort;
import com.example.trading.domain.enums.OrderSide;
import com.example.trading.domain.enums.OrderStatus;
import com.example.trading.domain.enums.OrderType;
import com.example.trading.domain.model.BookOrder;
import com.example.trading.domain.model.Order;
import com.example.trading.domain.model.OrderBook;
import com.example.trading.domain.model.Trade;
import com.example.trading.infrastructure.persistence.entity.OrderEntity;
import com.example.trading.infrastructure.persistence.entity.TradeEntity;
import com.example.trading.infrastructure.websocket.dto.PriceUpdate;
import com.example.trading.infrastructure.websocket.dto.TradeUpdate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepositoryPort orderRepository;
    private final TradeRepositoryPort tradeRepository;
    private final MarketDataPublisherPort marketPublisher;
    private final OrderBookStore bookStore;
    private final MatchingService matchingService;
    private final ClockProvider clock;

    // Per-symbol locks to keep matching atomic
    private final ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    public OrderEntity placeOrder(UUID userId, String symbol, OrderSide side, OrderType type, BigDecimal price, BigDecimal quantity) {
        Instant now = clock.now();
        validate(symbol, type, price, quantity);

        Order incoming = (type == OrderType.LIMIT)
                ? Order.newLimit(userId, symbol, side, price, quantity, now)
                : Order.newMarket(userId, symbol, side, quantity, now);

        ReentrantLock lock = locks.computeIfAbsent(symbol, s -> new ReentrantLock());
        lock.lock();
        try {
            OrderBook book = bookStore.getOrCreate(symbol);

            // Persist incoming order first
            OrderEntity incomingEntity = OrderEntity.fromDomain(incoming);
            incomingEntity = orderRepository.save(incomingEntity);

            // Load maker orders from book (in-memory IDs), then match against them
            List<OrderEntity> makers = loadMakerEntities(book, side);
            // Convert to domain for matching
            List<Order> makerDomain = makers.stream().map(OrderEntity::toDomain).toList();

            var result = matchingService.match(incoming, makerDomain, now);
            BigDecimal remaining = result.remainingQty();

            // Apply executions: update maker + taker remaining/status
            for (Trade t : result.trades()) {
                // Update maker orders
                OrderEntity buy = orderRepository.findById(t.getBuyOrderId()).orElseThrow();
                OrderEntity sell = orderRepository.findById(t.getSellOrderId()).orElseThrow();

                // Reduce quantities
                reduceOrder(buy, t.getQuantity());
                reduceOrder(sell, t.getQuantity());

                // Save updates
                orderRepository.save(buy);
                orderRepository.save(sell);

                // Persist trade
                TradeEntity te = TradeEntity.fromDomain(t);
                tradeRepository.save(te);

                // Publish trade + last price
                marketPublisher.publishTrade(TradeUpdate.from(te));
                marketPublisher.publishPrice(new PriceUpdate(symbol, t.getPrice(), now));
            }

            // Update incoming based on remaining
            incomingEntity.setRemainingQty(remaining);
            if (remaining.signum() == 0) {
                incomingEntity.setStatus(OrderStatus.FILLED);
            } else if (remaining.compareTo(quantity) < 0) {
                incomingEntity.setStatus(OrderStatus.PARTIALLY_FILLED);
            } else {
                incomingEntity.setStatus(OrderStatus.NEW);
            }
            incomingEntity = orderRepository.save(incomingEntity);

            // If it is LIMIT and not fully filled, add to book
            if (type == OrderType.LIMIT && incomingEntity.getStatus() != OrderStatus.FILLED) {
                book.sideQueue(side).add(new BookOrder(incomingEntity.getId(), userId, symbol, side, incomingEntity.getPrice(), incomingEntity.getCreatedAt()));
            }

            // Clean maker queue: remove any fully-filled makers from head; also remove stale references
            cleanupQueues(book, side);

            return incomingEntity;

        } finally {
            lock.unlock();
        }
    }

    public List<OrderEntity> myOrders(UUID userId) {
        return orderRepository.findByUserId(userId);
    }

    private void validate(String symbol, OrderType type, BigDecimal price, BigDecimal quantity) {
        if (symbol == null || symbol.isBlank()) throw new IllegalArgumentException("symbol required");
        if (quantity == null || quantity.signum() <= 0) throw new IllegalArgumentException("quantity must be > 0");
        if (type == OrderType.LIMIT) {
            if (price == null || price.signum() <= 0) throw new IllegalArgumentException("price must be > 0 for LIMIT");
        }
    }

    private List<OrderEntity> loadMakerEntities(OrderBook book, OrderSide incomingSide) {
        var oppQueue = book.oppositeQueue(incomingSide);
        // Snapshot current book (do not poll). For matching we want best orders first.
        List<BookOrder> snapshot = new ArrayList<>(oppQueue);

        // Sort snapshot same as queue order
        Comparator<BookOrder> comparator = incomingSide == OrderSide.BUY
                ? Comparator.comparing(BookOrder::price).thenComparing(BookOrder::createdAt)
                : Comparator.comparing(BookOrder::price, Comparator.reverseOrder()).thenComparing(BookOrder::createdAt);
        // But the actual queue comparator differs; to keep correct priority, re-sort using queue's natural order by copying into new PQ
        // Simpler: sort by price-time depending on side.
        snapshot.sort((a,b) -> {
            if (incomingSide == OrderSide.BUY) {
                // opposite is asks: lower price first
                int p = a.price().compareTo(b.price());
                if (p != 0) return p;
                return a.createdAt().compareTo(b.createdAt());
            } else {
                // opposite is bids: higher price first
                int p = b.price().compareTo(a.price());
                if (p != 0) return p;
                return a.createdAt().compareTo(b.createdAt());
            }
        });

        List<OrderEntity> makers = new ArrayList<>();
        for (BookOrder bo : snapshot) {
            orderRepository.findById(bo.orderId()).ifPresent(makers::add);
        }
        return makers;
    }

    private void reduceOrder(OrderEntity e, BigDecimal executedQty) {
        BigDecimal remaining = e.getRemainingQty().subtract(executedQty);
        e.setRemainingQty(remaining);
        if (remaining.signum() == 0) {
            e.setStatus(OrderStatus.FILLED);
        } else {
            e.setStatus(OrderStatus.PARTIALLY_FILLED);
        }
    }

    private void cleanupQueues(OrderBook book, OrderSide incomingSide) {
        var oppQueue = book.oppositeQueue(incomingSide);
        // Remove filled orders from head
        while (!oppQueue.isEmpty()) {
            BookOrder head = oppQueue.peek();
            var eOpt = orderRepository.findById(head.orderId());
            if (eOpt.isEmpty()) {
                oppQueue.poll();
                continue;
            }
            if (eOpt.get().getStatus() == OrderStatus.FILLED) {
                oppQueue.poll();
            } else {
                break;
            }
        }
    }
}
