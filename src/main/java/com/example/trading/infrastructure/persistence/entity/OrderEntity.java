package com.example.trading.infrastructure.persistence.entity;

import com.example.trading.domain.enums.OrderSide;
import com.example.trading.domain.enums.OrderStatus;
import com.example.trading.domain.enums.OrderType;
import com.example.trading.domain.model.Order;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String symbol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderSide side;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType type;

    @Column(precision = 19, scale = 6)
    private BigDecimal price;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal quantity;

    @Column(name = "remaining_qty", nullable = false, precision = 19, scale = 6)
    private BigDecimal remainingQty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public static OrderEntity fromDomain(Order o) {
        return OrderEntity.builder()
                .id(o.getId())
                .userId(o.getUserId())
                .symbol(o.getSymbol())
                .side(o.getSide())
                .type(o.getType())
                .price(o.getPrice())
                .quantity(o.getQuantity())
                .remainingQty(o.getRemainingQuantity())
                .status(o.getStatus())
                .createdAt(o.getCreatedAt())
                .build();
    }

    public Order toDomain() {
        return new Order(id, userId, symbol, side, type, price, quantity, remainingQty, status, createdAt);
    }
}
