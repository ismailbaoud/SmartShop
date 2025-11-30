package com.ismail.smartShop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismail.smartShop.annotation.RequireAdmin;
import com.ismail.smartShop.annotation.RequireAuth;
import com.ismail.smartShop.annotation.RequireClient;
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
    @RequireAuth
    @RequireAdmin
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest OrderReq) {
        return ResponseEntity.ok().body(orderService.createOrder(OrderReq));
    }

    @GetMapping
    @RequireAuth
    @RequireAdmin
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok().body(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    @RequireAuth
    public ResponseEntity<List<OrderResponse>> getAllOrdersOfClient(@PathVariable Long id) {
        return ResponseEntity.ok().body(orderService.getAllOrdersOfClient(id));
    }
    
    @PutMapping("/{id}/cancle")
    @RequireAuth
    @RequireAdmin
    public ResponseEntity<OrderResponse> cancleOrder(@PathVariable Long id) {
        return ResponseEntity.ok().body(orderService.cancelOrder(id));
    }
    
    @PutMapping("/{id}/confirm")
    @RequireAuth
    @RequireAdmin
    public ResponseEntity<OrderResponse> putMethodName(@PathVariable Long id) {
        return ResponseEntity.ok().body(orderService.confirmOrder(id));

    }
}
