package com.ismail.smartShop.dto.payment.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentRequest {

    private Double amount;

    private String method;

    private String banque;

}
