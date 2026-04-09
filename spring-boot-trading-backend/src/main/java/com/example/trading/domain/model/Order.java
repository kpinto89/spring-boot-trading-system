package com.example.trading.domain.model;

import com.example.trading.domain.enums.OrderSide;
import com.example.trading.domain.enums.OrderStatus;
import com.example.trading.domain.enums.OrderType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Order {
    private final UUID id;
    private final UUID userId;
    private final String symbol;
    private final OrderSide side;
    private final OrderType type;
    private final BigDecimal price; // nullable for MARKET
    private final BigDecimal quantity;
    private BigDecimal remainingQuantity;
    private OrderStatus status;
    private final Instant createdAt;

    public Order(UUID id, UUID userId, String symbol, OrderSide side, OrderType type,
                 BigDecimal price, BigDecimal quantity, BigDecimal remainingQuantity,
                 OrderStatus status, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.symbol = symbol;
        this.side = side;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.remainingQuantity = remainingQuantity;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static Order newLimit(UUID userId, String symbol, OrderSide side, BigDecimal price, BigDecimal qty, Instant now) {
        UUID id = UUID.randomUUID();
        return new Order(id, userId, symbol, side, OrderType.LIMIT, price, qty, qty, OrderStatus.NEW, now);
    }

    public static Order newMarket(UUID userId, String symbol, OrderSide side, BigDecimal qty, Instant now) {
        UUID id = UUID.randomUUID();
        return new Order(id, userId, symbol, side, OrderType.MARKET, null, qty, qty, OrderStatus.NEW, now);
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public String getSymbol() { return symbol; }
    public OrderSide getSide() { return side; }
    public OrderType getType() { return type; }
    public BigDecimal getPrice() { return price; }
    public BigDecimal getQuantity() { return quantity; }
    public BigDecimal getRemainingQuantity() { return remainingQuantity; }
    public OrderStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }

    public boolean isBuy() { return side == OrderSide.BUY; }

    public void reduce(BigDecimal executedQty) {
        this.remainingQuantity = this.remainingQuantity.subtract(executedQty);
        if (this.remainingQuantity.signum() == 0) {
            this.status = OrderStatus.FILLED;
        } else {
            this.status = OrderStatus.PARTIALLY_FILLED;
        }
    }

    public void markRejected() {
        this.status = OrderStatus.REJECTED;
    }
}
