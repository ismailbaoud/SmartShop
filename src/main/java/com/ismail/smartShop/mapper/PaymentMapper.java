package com.ismail.smartShop.mapper;

import org.mapstruct.Mapper;

import com.ismail.smartShop.dto.payment.request.PaymentRequest;
import com.ismail.smartShop.dto.payment.response.PaymentResponse;
import com.ismail.smartShop.model.Payment;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentResponse toDto(Payment p);
    Payment toEntity(PaymentRequest pReq);
}
