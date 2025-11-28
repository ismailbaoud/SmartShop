package com.ismail.smartShop.dto.payment.response;

import java.time.LocalDateTime;

import com.ismail.smartShop.model.Order;
import com.ismail.smartShop.model.enums.PaymentMethod;

public record PaymentResponse(
    Long id,

    String amount,

    PaymentMethod method,


    String Banque,

    LocalDateTime datePaiement,

    LocalDateTime dateEncaissement,

    Order order

) {}
