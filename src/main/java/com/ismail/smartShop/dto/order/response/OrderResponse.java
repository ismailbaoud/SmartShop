package com.ismail.smartShop.dto.order.response;

import java.time.LocalDateTime;
import java.util.List;

import com.ismail.smartShop.model.Client;
import com.ismail.smartShop.model.Payment;
import com.ismail.smartShop.model.Promo;
import com.ismail.smartShop.model.enums.OrderStatus;

public record OrderResponse(
    Long id,
    String promocode,
    Client client,
    List<Payment> payments,
    LocalDateTime dateOrder,
    Double subTotal,
    Double htAfterDiscount,
    Double tva,
    Double totalTTC,
    OrderStatus status,
    Double montant_restant

) {}
