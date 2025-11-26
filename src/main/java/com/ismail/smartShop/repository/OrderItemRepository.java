package com.ismail.smartShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ismail.smartShop.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{
    
}
