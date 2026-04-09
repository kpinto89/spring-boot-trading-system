package com.example.trading.infrastructure.persistence.adapter;

import com.example.trading.application.port.output.WalletRepositoryPort;
import com.example.trading.infrastructure.persistence.entity.WalletEntity;
import com.example.trading.infrastructure.persistence.repository.JpaWalletRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WalletRepositoryAdapter implements WalletRepositoryPort {

    private final JpaWalletRepository repo;

    @Override
    public Optional<WalletEntity> findByUserId(UUID userId) {
        return repo.findByUserId(userId);
    }

    @Override
    public WalletEntity save(WalletEntity wallet) {
        return repo.save(wallet);
    }
}
