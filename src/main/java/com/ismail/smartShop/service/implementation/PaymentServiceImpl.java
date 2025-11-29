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
import com.ismail.smartShop.model.enums.OrderStatus;
import com.ismail.smartShop.model.enums.PaymentMethod;
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
    private final ClientServiceImpl clientService;

    @Override
    public PaymentResponse createPayment(Long id, PaymentRequest paymentReq) {
        Order order = orderRepository.findById(id).orElseThrow(()-> new RuntimeException());
        Client client = order.getClient();
        Payment payment = paymentMapper.toEntity(paymentReq);
        payment.setOrder(order);
        payment.setBanque(paymentReq.getBanque());
        payment.setDatePaiement(LocalDateTime.now());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setMethod(PaymentMethod.valueOf(paymentReq.getMethod()));
        if (order.getMontant_restant() < payment.getAmount()) {
            throw new RuntimeException("this amount is haist than the rest amoun , plase enter just the rest amount : "+order.getMontant_restant());
        }
        
        PaymentResponse pr = paymentMapper.toDto(paymentRepository.save(payment));
        order.setMontant_restant(order.getMontant_restant() - payment.getAmount());
        if(order.getMontant_restant() == 0) {
            order.setStatus(OrderStatus.CONFIRMED);
            client.setTotalCommandes(client.getTotalCommandes() + 1);
            client.setTotalDepense(client.getTotalDepense() + payment.getAmount());
            client = clientService.changeFidelite(client);
            clientRepository.save(client);
        }
        orderRepository.save(order);
        return pr;
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