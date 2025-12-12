package com.ismail.smartShop.dto.orderItem.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ismail.smartShop.model.Client;
import com.ismail.smartShop.model.Product;

public record OrderItemResponse(
    Product product,
    Integer quantite,
    Double prixUnitaire,
    Double linkTotal,
    @JsonIgnore
    Client client
) {}
