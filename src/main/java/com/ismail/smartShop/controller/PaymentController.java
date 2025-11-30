package com.ismail.smartShop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismail.smartShop.annotation.RequireAdmin;
import com.ismail.smartShop.annotation.RequireAuth;
import com.ismail.smartShop.dto.payment.request.PaymentRequest;
import com.ismail.smartShop.dto.payment.response.PaymentResponse;
import com.ismail.smartShop.service.implementation.PaymentServiceImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class PaymentController {
    
    private final PaymentServiceImpl paymentService;

    @PostMapping("/{id}/payments")
    @RequireAuth
    @RequireAdmin
    public PaymentResponse payOrder(@PathVariable("id") Long id ,@RequestBody PaymentRequest paymentReq) {
        return paymentService.createPayment(id, paymentReq);
    }
    

}
