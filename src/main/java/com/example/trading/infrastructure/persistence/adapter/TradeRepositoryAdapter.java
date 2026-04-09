package com.example.trading.infrastructure.persistence.adapter;

import com.example.trading.application.port.output.TradeRepositoryPort;
import com.example.trading.infrastructure.persistence.entity.TradeEntity;
import com.example.trading.infrastructure.persistence.repository.JpaTradeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TradeRepositoryAdapter implements TradeRepositoryPort {

    private final JpaTradeRepository repo;

    @Override
    public TradeEntity save(TradeEntity trade) {
        return repo.save(trade);
    }
}
