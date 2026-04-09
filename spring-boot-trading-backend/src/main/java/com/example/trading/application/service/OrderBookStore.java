package com.example.trading.application.service;

import com.example.trading.domain.model.BookOrder;
import com.example.trading.domain.model.OrderBook;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderBookStore {

    private final Map<String, OrderBook> books = new ConcurrentHashMap<>();

    public OrderBook getOrCreate(String symbol) {
        return books.computeIfAbsent(symbol, s -> new OrderBook(
                new PriorityQueue<>(bidComparator()),
                new PriorityQueue<>(askComparator())
        ));
    }

    private Comparator<BookOrder> bidComparator() {
        // Higher price first, then older first (price-time priority)
        return Comparator
                .comparing(BookOrder::price, Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(BookOrder::createdAt);
    }

    private Comparator<BookOrder> askComparator() {
        // Lower price first, then older first
        return Comparator
                .comparing(BookOrder::price, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(BookOrder::createdAt);
    }
}
