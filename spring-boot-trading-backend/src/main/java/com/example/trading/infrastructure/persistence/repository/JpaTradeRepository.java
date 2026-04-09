package com.example.trading.infrastructure.persistence.repository;

import com.example.trading.infrastructure.persistence.entity.TradeEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaTradeRepository extends JpaRepository<TradeEntity, UUID> {
    Optional<TradeEntity> findTopBySymbolOrderByExecutedAtDesc(String symbol);
}
