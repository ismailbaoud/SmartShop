package com.ismail.smartShop.dto.orderItem.response;

import java.math.BigDecimal;

import com.ismail.smartShop.model.Product;

public record OrderItemResponse(
    Product product,
    Integer quantite,
    BigDecimal prixUnitaire,
    BigDecimal linkTotal
) {}
