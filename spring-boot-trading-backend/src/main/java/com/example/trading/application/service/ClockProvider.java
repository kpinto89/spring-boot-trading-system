package com.example.trading.application.service;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ClockProvider {
    public Instant now() {
        return Instant.now();
    }
}
