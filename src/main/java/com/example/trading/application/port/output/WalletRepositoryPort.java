package com.example.trading.application.port.output;

import com.example.trading.infrastructure.persistence.entity.WalletEntity;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepositoryPort {
    Optional<WalletEntity> findByUserId(UUID userId);
    WalletEntity save(WalletEntity wallet);
}
