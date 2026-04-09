package com.example.trading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class TradingApplication {
    public static void main(String[] args) {
        // Use a canonical zone so pgjdbc doesn't send locale-specific aliases (e.g., Asia/Calcutta).
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(TradingApplication.class, args);
    }
}
