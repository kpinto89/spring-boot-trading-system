package com.example.trading.api.controller;

import com.example.trading.api.request.PlaceOrderRequest;
import com.example.trading.api.response.OrderResponse;
import com.example.trading.application.service.OrderService;
import com.example.trading.security.AuthenticatedUser;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> place(@AuthenticatedUser java.util.UUID userId,
                                              @Valid @RequestBody PlaceOrderRequest req) {
        var e = orderService.placeOrder(userId, req.symbol(), req.side(), req.type(), req.price(), req.quantity());
        return ResponseEntity.ok(OrderResponse.from(e));
    }

    @GetMapping("/me")
    public ResponseEntity<List<OrderResponse>> myOrders(@AuthenticatedUser java.util.UUID userId) {
        var list = orderService.myOrders(userId).stream().map(OrderResponse::from).toList();
        return ResponseEntity.ok(list);
    }
}
