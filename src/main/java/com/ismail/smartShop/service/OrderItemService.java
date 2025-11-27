package com.ismail.smartShop.service;

import java.util.List;

import com.ismail.smartShop.dto.orderItem.Request.OrderItemRequest;
import com.ismail.smartShop.dto.orderItem.response.OrderItemResponse;
import com.ismail.smartShop.model.Order;

public interface OrderItemService {
    public OrderItemResponse createOrderItem(OrderItemRequest req, Order order);

    public List<OrderItemResponse> getAllOrderItem();

}
