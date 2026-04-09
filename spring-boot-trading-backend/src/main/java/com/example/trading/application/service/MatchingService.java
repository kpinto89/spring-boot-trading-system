package com.example.trading.application.service;

import com.example.trading.domain.enums.OrderSide;
import com.example.trading.domain.enums.OrderType;
import com.example.trading.domain.model.Order;
import com.example.trading.domain.model.Trade;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MatchingService {

    public record MatchResult(List<Trade> trades, BigDecimal remainingQty) {}

    public MatchResult match(Order incoming, List<Order> oppositeOrders, Instant now) {
        List<Trade> trades = new ArrayList<>();
        BigDecimal remaining = incoming.getRemainingQuantity();

        for (Order maker : oppositeOrders) {
            if (remaining.signum() == 0) break;
            if (maker.getRemainingQuantity().signum() == 0) continue;

            if (!isPriceMatch(incoming, maker)) {
                // For MARKET order, price match is always true (we execute at maker price)
                if (incoming.getType() == OrderType.MARKET) {
                    // ok
                } else {
                    continue;
                }
            }

            BigDecimal execQty = remaining.min(maker.getRemainingQuantity());
            BigDecimal execPrice = maker.getPrice();
            if (execPrice == null) {
                // Maker should be LIMIT (in this MVP). If not, skip.
                continue;
            }

            UUID tradeId = UUID.randomUUID();
            UUID buyId = incoming.getSide() == OrderSide.BUY ? incoming.getId() : maker.getId();
            UUID sellId = incoming.getSide() == OrderSide.SELL ? incoming.getId() : maker.getId();

            trades.add(new Trade(tradeId, incoming.getSymbol(), buyId, sellId, execPrice, execQty, now));

            remaining = remaining.subtract(execQty);
        }

        return new MatchResult(trades, remaining);
    }

    private boolean isPriceMatch(Order taker, Order maker) {
        if (taker.getPrice() == null || maker.getPrice() == null) return false;
        return taker.isBuy()
                ? taker.getPrice().compareTo(maker.getPrice()) >= 0
                : taker.getPrice().compareTo(maker.getPrice()) <= 0;
    }
}
