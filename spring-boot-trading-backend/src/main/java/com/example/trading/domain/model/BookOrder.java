package com.example.trading.domain.model;

import com.example.trading.domain.enums.OrderSide;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record BookOrder(
        UUID orderId,
        UUID userId,
        String symbol,
        OrderSide side,
        BigDecimal price,
        Instant createdAt
) {}
