package com.example.trading.domain.model;

import com.example.trading.domain.enums.OrderSide;

import java.util.PriorityQueue;

public class OrderBook {
    private final PriorityQueue<BookOrder> bids;
    private final PriorityQueue<BookOrder> asks;

    public OrderBook(PriorityQueue<BookOrder> bids, PriorityQueue<BookOrder> asks) {
        this.bids = bids;
        this.asks = asks;
    }

    public PriorityQueue<BookOrder> getBids() { return bids; }
    public PriorityQueue<BookOrder> getAsks() { return asks; }

    public PriorityQueue<BookOrder> sideQueue(OrderSide side) {
        return side == OrderSide.BUY ? bids : asks;
    }

    public PriorityQueue<BookOrder> oppositeQueue(OrderSide side) {
        return side == OrderSide.BUY ? asks : bids;
    }
}
