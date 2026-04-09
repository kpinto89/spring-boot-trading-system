package com.example.trading.infrastructure.persistence.entity;

import com.example.trading.domain.model.Trade;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "trades")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String symbol;

    @Column(name = "buy_order_id", nullable = false)
    private UUID buyOrderId;

    @Column(name = "sell_order_id", nullable = false)
    private UUID sellOrderId;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal price;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal quantity;

    @Column(name = "executed_at", nullable = false)
    private Instant executedAt;

    public static TradeEntity fromDomain(Trade t) {
        return TradeEntity.builder()
                .id(t.getId())
                .symbol(t.getSymbol())
                .buyOrderId(t.getBuyOrderId())
                .sellOrderId(t.getSellOrderId())
                .price(t.getPrice())
                .quantity(t.getQuantity())
                .executedAt(t.getExecutedAt())
                .build();
    }
}
