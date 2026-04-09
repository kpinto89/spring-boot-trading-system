package com.example.trading.application.port.output;

import com.example.trading.infrastructure.persistence.entity.OrderEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepositoryPort {
    OrderEntity save(OrderEntity order);
    Optional<OrderEntity> findById(UUID id);
    List<OrderEntity> findByUserId(UUID userId);
}
