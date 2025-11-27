package com.ismail.smartShop.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ismail.smartShop.dto.orderItem.Request.OrderItemRequest;
import com.ismail.smartShop.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Collection<OrderItemRequest> findAllByClientId(Long id);

}
