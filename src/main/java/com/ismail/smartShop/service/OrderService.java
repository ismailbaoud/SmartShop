package com.ismail.smartShop.service;

import org.springframework.stereotype.Service;

import com.ismail.smartShop.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

}
