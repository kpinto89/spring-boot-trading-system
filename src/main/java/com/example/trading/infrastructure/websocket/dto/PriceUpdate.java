package com.example.trading.infrastructure.websocket.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record PriceUpdate(
        String symbol,
        BigDecimal price,
        Instant at
) {}
