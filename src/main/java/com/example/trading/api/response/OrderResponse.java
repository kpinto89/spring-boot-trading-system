package com.example.trading.api.response;

import com.example.trading.domain.enums.OrderSide;
import com.example.trading.domain.enums.OrderStatus;
import com.example.trading.domain.enums.OrderType;
import com.example.trading.infrastructure.persistence.entity.OrderEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        String symbol,
        OrderSide side,
        OrderType type,
        BigDecimal price,
        BigDecimal quantity,
        BigDecimal remainingQty,
        OrderStatus status,
        Instant createdAt
) {
    public static OrderResponse from(OrderEntity e) {
        return new OrderResponse(
                e.getId(), e.getSymbol(), e.getSide(), e.getType(), e.getPrice(),
                e.getQuantity(), e.getRemainingQty(), e.getStatus(), e.getCreatedAt()
        );
    }
}
