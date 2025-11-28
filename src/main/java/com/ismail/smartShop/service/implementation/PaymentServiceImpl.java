package com.ismail.smartShop.service.implementation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ismail.smartShop.dto.payment.request.PaymentRequest;
import com.ismail.smartShop.dto.payment.request.PaymentStatusRequest;
import com.ismail.smartShop.dto.payment.response.PaymentResponse;
import com.ismail.smartShop.mapper.PaymentMapper;
import com.ismail.smartShop.model.Client;
import com.ismail.smartShop.model.Order;
import com.ismail.smartShop.model.Payment;
import com.ismail.smartShop.model.enums.PaymentStatus;
import com.ismail.smartShop.repository.ClientRepository;
import com.ismail.smartShop.repository.OrderRepository;
import com.ismail.smartShop.repository.PaymentRepository;
import com.ismail.smartShop.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;

    @Override
    public PaymentResponse createPayment(Long id, PaymentRequest paymentReq) {
        Order order = orderRepository.findById(id).orElseThrow(()-> new RuntimeException());

        Payment payment = paymentMapper.toEntity(paymentReq);
        payment.setOrder(order);
        return paymentMapper.toDto(paymentRepository.save(payment));
    }

    @Override
    public List<PaymentResponse> getPaymentsOfOrder(Long id) {
        return paymentRepository.findAll().stream().filter(a -> a.getId() == id).map(a -> paymentMapper.toDto(a)).toList();
    }

    @Override
    public PaymentResponse updatePaymentStatus(Long orderId, Long paymentId, PaymentStatusRequest payStatusReq) {
        Payment payment = paymentRepository.findAll().stream().filter(a -> a.getOrder().getId() == orderId &&  a.getId() == paymentId).findFirst().orElseThrow(()-> new RuntimeException());
        payment.setStatus(PaymentStatus.valueOf(payStatusReq.getStatus()));
        payment.setDateEncaissement(LocalDateTime.now());
        return paymentMapper.toDto(paymentRepository.save(payment));
    }

}