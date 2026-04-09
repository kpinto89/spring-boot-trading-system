package com.example.trading.application.service;

import com.example.trading.domain.enums.OrderSide;
import com.example.trading.domain.model.Order;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class MatchingServiceTest {

    private final MatchingService svc = new MatchingService();

    @Test
    void matchesBestAskForBuyLimit() {
        Instant now = Instant.parse("2026-01-01T00:00:00Z");
        UUID makerUser = UUID.randomUUID();
        UUID takerUser = UUID.randomUUID();

        // Maker asks: 180 then 181
        Order ask180 = Order.newLimit(makerUser, "AAPL", OrderSide.SELL, new BigDecimal("180"), new BigDecimal("10"), now);
        Order ask181 = Order.newLimit(makerUser, "AAPL", OrderSide.SELL, new BigDecimal("181"), new BigDecimal("10"), now.plusSeconds(1));

        // Buy limit at 181 for qty 5 -> should fill at 180
        Order buy = Order.newLimit(takerUser, "AAPL", OrderSide.BUY, new BigDecimal("181"), new BigDecimal("5"), now.plusSeconds(2));

        var result = svc.match(buy, List.of(ask180, ask181), now.plusSeconds(3));
        assertThat(result.trades()).hasSize(1);
        assertThat(result.trades().get(0).getPrice()).isEqualByComparingTo("180");
        assertThat(result.trades().get(0).getQuantity()).isEqualByComparingTo("5");
        assertThat(result.remainingQty()).isEqualByComparingTo("0");
    }
}
