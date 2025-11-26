package com.ismail.smartShop.dto.orderItem.Request;

import com.ismail.smartShop.model.Product;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Long product_id;
    private Integer quantite;
}
