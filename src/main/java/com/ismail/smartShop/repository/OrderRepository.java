package com.ismail.smartShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ismail.smartShop.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
