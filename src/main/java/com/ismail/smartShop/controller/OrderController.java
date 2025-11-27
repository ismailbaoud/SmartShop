package com.ismail.smartShop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismail.smartShop.dto.order.request.OrderRequest;
import com.ismail.smartShop.dto.order.response.OrderResponse;
import com.ismail.smartShop.service.implementation.OrderServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;
    
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest OrderReq) {
        return ResponseEntity.ok().body(orderService.createOrder(OrderReq));
    }
    
}
