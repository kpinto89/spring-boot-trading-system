package com.example.trading.application.port.output;

import com.example.trading.infrastructure.websocket.dto.PriceUpdate;
import com.example.trading.infrastructure.websocket.dto.TradeUpdate;

public interface MarketDataPublisherPort {
    void publishTrade(TradeUpdate trade);
    void publishPrice(PriceUpdate price);
}
