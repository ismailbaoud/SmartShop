package com.ismail.smartShop.service;

import java.util.List;

import com.ismail.smartShop.dto.payment.request.PaymentRequest;
import com.ismail.smartShop.dto.payment.request.PaymentStatusRequest;
import com.ismail.smartShop.dto.payment.response.PaymentResponse;

public interface PaymentService {
    
    public PaymentResponse createPayment(Long id , PaymentRequest paymentReq);

    public List<PaymentResponse> getPaymentsOfOrder(Long id);

    public PaymentResponse updatePaymentStatus(Long iorderId , Long paymentId, PaymentStatusRequest payStatusReq);
}
