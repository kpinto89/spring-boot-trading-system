package com.example.trading.infrastructure.persistence.repository;

import com.example.trading.infrastructure.persistence.entity.WalletEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaWalletRepository extends JpaRepository<WalletEntity, UUID> {
    Optional<WalletEntity> findByUserId(UUID userId);
}
