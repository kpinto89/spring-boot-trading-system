package com.example.trading.api.request;

import com.example.trading.domain.enums.OrderSide;
import com.example.trading.domain.enums.OrderType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PlaceOrderRequest(
        @NotBlank String symbol,
        @NotNull OrderSide side,
        @NotNull OrderType type,
        BigDecimal price,
        @NotNull @Positive BigDecimal quantity
) {}
