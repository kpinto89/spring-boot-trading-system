package com.example.trading.infrastructure.websocket;

import com.example.trading.application.port.output.MarketDataPublisherPort;
import com.example.trading.infrastructure.websocket.dto.PriceUpdate;
import com.example.trading.infrastructure.websocket.dto.TradeUpdate;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MarketDataWebSocketPublisher implements MarketDataPublisherPort {

    private final SimpMessagingTemplate template;

    @Override
    public void publishTrade(TradeUpdate trade) {
        template.convertAndSend("/topic/trades/" + trade.symbol(), trade);
    }

    @Override
    public void publishPrice(PriceUpdate price) {
        template.convertAndSend("/topic/price/" + price.symbol(), price);
    }
}
