package com.example.trading.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Trade {
    private final UUID id;
    private final String symbol;
    private final UUID buyOrderId;
    private final UUID sellOrderId;
    private final BigDecimal price;
    private final BigDecimal quantity;
    private final Instant executedAt;

    public Trade(UUID id, String symbol, UUID buyOrderId, UUID sellOrderId, BigDecimal price, BigDecimal quantity, Instant executedAt) {
        this.id = id;
        this.symbol = symbol;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.price = price;
        this.quantity = quantity;
        this.executedAt = executedAt;
    }

    public UUID getId() { return id; }
    public String getSymbol() { return symbol; }
    public UUID getBuyOrderId() { return buyOrderId; }
    public UUID getSellOrderId() { return sellOrderId; }
    public BigDecimal getPrice() { return price; }
    public BigDecimal getQuantity() { return quantity; }
    public Instant getExecutedAt() { return executedAt; }
}
