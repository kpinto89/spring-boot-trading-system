package com.example.trading.infrastructure.persistence.repository;

import com.example.trading.infrastructure.persistence.entity.OrderEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaOrderRepository extends JpaRepository<OrderEntity, UUID> {
    List<OrderEntity> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
