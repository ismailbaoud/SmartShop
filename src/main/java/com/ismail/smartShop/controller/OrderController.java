package com.ismail.smartShop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismail.smartShop.dto.order.request.OrderRequest;
import com.ismail.smartShop.dto.order.response.OrderResponse;
import com.ismail.smartShop.service.implementation.OrderServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;
    
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest OrderReq) {
        return ResponseEntity.ok().body(orderService.createOrder(OrderReq));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok().body(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<OrderResponse>> getAllOrdersOfClient(@PathVariable Long id) {
        return ResponseEntity.ok().body(orderService.getAllOrdersOfClient(id));
    }
    
    @PutMapping("/{id}/cancle")
    public ResponseEntity<OrderResponse> cancleOrder(@PathVariable Long id) {
        return ResponseEntity.ok().body(orderService.cancelOrder(id));
    }
    
    @PutMapping("/{id}/confirm")
    public ResponseEntity<OrderResponse> putMethodName(@PathVariable Long id) {
        return ResponseEntity.ok().body(orderService.confirmOrder(id));

    }
}
