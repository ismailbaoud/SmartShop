package com.ismail.smartShop.dto.order.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ismail.smartShop.dto.orderItem.response.OrderItemResponse;
import com.ismail.smartShop.model.Client;
import com.ismail.smartShop.model.OrderItem;
import com.ismail.smartShop.model.Payment;
import com.ismail.smartShop.model.enums.OrderStatus;

public record OrderResponse(
    Long id,
    String promo,
    @JsonIgnore
    Client client,
    List<Payment> payments,
    LocalDateTime dateOrder,
    Double subTotal,
    Double htAfterDiscount,
    Double tva,
    Double totalTTC,
    OrderStatus status,
    Double montant_restant,
    List<OrderItemResponse> orderItems


) {}
