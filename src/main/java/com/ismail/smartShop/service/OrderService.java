package com.ismail.smartShop.service;

import com.ismail.smartShop.dto.order.request.OrderRequest;
import com.ismail.smartShop.dto.order.response.OrderResponse;

public interface OrderService {
    public OrderResponse createOrder(OrderRequest orderRequest);
}
