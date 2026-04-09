package com.example.trading.infrastructure.websocket.dto;

import com.example.trading.infrastructure.persistence.entity.TradeEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TradeUpdate(
        UUID id,
        String symbol,
        UUID buyOrderId,
        UUID sellOrderId,
        BigDecimal price,
        BigDecimal quantity,
        Instant executedAt
) {
    public static TradeUpdate from(TradeEntity e) {
        return new TradeUpdate(e.getId(), e.getSymbol(), e.getBuyOrderId(), e.getSellOrderId(), e.getPrice(), e.getQuantity(), e.getExecutedAt());
    }
}
