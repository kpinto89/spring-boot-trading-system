package com.example.trading.api.controller;

import com.example.trading.infrastructure.persistence.repository.JpaTradeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
public class MarketController {

    private final JpaTradeRepository tradeRepository;

    @GetMapping("/last-price")
    public ResponseEntity<BigDecimal> lastPrice(@RequestParam String symbol) {
        return ResponseEntity.ok(
                tradeRepository.findTopBySymbolOrderByExecutedAtDesc(symbol)
                        .map(t -> t.getPrice())
                        .orElse(BigDecimal.ZERO)
        );
    }
}
