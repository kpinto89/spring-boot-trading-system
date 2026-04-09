package com.example.trading.infrastructure.persistence.adapter;

import com.example.trading.application.port.output.OrderRepositoryPort;
import com.example.trading.infrastructure.persistence.entity.OrderEntity;
import com.example.trading.infrastructure.persistence.repository.JpaOrderRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final JpaOrderRepository repo;

    @Override
    public OrderEntity save(OrderEntity order) {
        return repo.save(order);
    }

    @Override
    public Optional<OrderEntity> findById(UUID id) {
        return repo.findById(id);
    }

    @Override
    public List<OrderEntity> findByUserId(UUID userId) {
        return repo.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
