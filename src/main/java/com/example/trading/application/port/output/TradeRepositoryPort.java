package com.example.trading.application.port.output;

import com.example.trading.infrastructure.persistence.entity.TradeEntity;

public interface TradeRepositoryPort {
    TradeEntity save(TradeEntity trade);
}
