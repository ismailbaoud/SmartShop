package com.ismail.smartShop.dto.order.request;

import java.util.List;

import com.ismail.smartShop.dto.orderItem.Request.OrderItemRequest;

import lombok.Data;

@Data
public class OrderRequest {
    private Long client_id;
    private String promo;
    private List<OrderItemRequest> items;
}
