package com.ismail.smartShop.service;

import java.util.List;

import com.ismail.smartShop.dto.order.request.OrderRequest;
import com.ismail.smartShop.dto.order.response.OrderResponse;

public interface OrderService {
    public OrderResponse createOrder(OrderRequest orderRequest);

    public List<OrderResponse> getAllOrders();

    public List<OrderResponse> getAllOrdersOfClient(Long id);

    public OrderResponse cancelOrder(Long id);

    public OrderResponse confirmOrder(Long id);
}
