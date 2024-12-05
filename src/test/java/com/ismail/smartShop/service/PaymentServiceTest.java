package com.ismail.smartShop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ismail.smartShop.dto.payment.request.PaymentRequest;
import com.ismail.smartShop.dto.payment.request.PaymentStatusRequest;
import com.ismail.smartShop.dto.payment.response.PaymentResponse;
import com.ismail.smartShop.mapper.PaymentMapper;
import com.ismail.smartShop.model.Client;
import com.ismail.smartShop.model.Order;
import com.ismail.smartShop.model.Payment;
import com.ismail.smartShop.model.enums.NiveauFidelite;
import com.ismail.smartShop.model.enums.OrderStatus;
import com.ismail.smartShop.model.enums.PaymentMethod;
import com.ismail.smartShop.model.enums.PaymentStatus;
import com.ismail.smartShop.repository.ClientRepository;
import com.ismail.smartShop.repository.OrderRepository;
import com.ismail.smartShop.repository.PaymentRepository;
import com.ismail.smartShop.service.implementation.ClientServiceImpl;
import com.ismail.smartShop.service.implementation.PaymentServiceImpl;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientServiceImpl clientService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Payment payment;
    private PaymentRequest paymentRequest;
    private PaymentResponse paymentResponse;
    private Order order;
    private Client client;

    @BeforeEach
    void setUp() {
        client = Client.builder()
            .id(1L)
            .nom("Test Client")
            .email("client@test.com")
            .niveauDeFidelite(NiveauFidelite.BASIC)
            .totalCommandes(0)
            .totalDepense(0.0)
            .build();

        order = new Order();
        order.setId(1L);
        order.setClient(client);
        order.setTotalTTC(1000.0);
        order.setMontant_restant(1000.0);
        order.setStatus(OrderStatus.PANDING);

        payment = new Payment();
        payment.setId(1L);
        payment.setAmount(500.0);
        payment.setMethod(PaymentMethod.ESPECES);
        payment.setBanque("BankTest");
        payment.setStatus(PaymentStatus.PENDING);
        payment.setOrder(order);

        paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(500.0);
        paymentRequest.setMethod("ESPECES");
        paymentRequest.setBanque("BankTest");

        paymentResponse = new PaymentResponse(
            1L,
            "500.0",
            PaymentMethod.ESPECES,
            "BankTest",
            LocalDateTime.now(),
            null,
            order
        );
    }

    @Test
    void createPayment_ShouldCreatePayment_WhenValidAmount() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentMapper.toEntity(paymentRequest)).thenReturn(payment);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(paymentMapper.toDto(payment)).thenReturn(paymentResponse);
        when(orderRepository.save(order)).thenReturn(order);

        // Act
        PaymentResponse result = paymentService.createPayment(1L, paymentRequest);

        // Assert
        assertNotNull(result);
        assertEquals(500.0, order.getMontant_restant());
        verify(orderRepository, times(1)).findById(1L);
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void createPayment_ShouldConfirmOrder_WhenFullPayment() {
        // Arrange
        paymentRequest.setAmount(1000.0);
        payment.setAmount(1000.0);
        
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentMapper.toEntity(paymentRequest)).thenReturn(payment);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(paymentMapper.toDto(payment)).thenReturn(paymentResponse);
        when(clientService.changeFidelite(client)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(client);
        when(orderRepository.save(order)).thenReturn(order);

        // Act
        PaymentResponse result = paymentService.createPayment(1L, paymentRequest);

        // Assert
        assertNotNull(result);
        assertEquals(0.0, order.getMontant_restant());
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
        assertEquals(1, client.getTotalCommandes());
        verify(clientService, times(1)).changeFidelite(client);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void createPayment_ShouldThrowException_WhenAmountExceedsRemaining() {
        // Arrange
        paymentRequest.setAmount(1500.0);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentMapper.toEntity(paymentRequest)).thenReturn(payment);
        payment.setAmount(1500.0);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> paymentService.createPayment(1L, paymentRequest));
        
        assertTrue(exception.getMessage().contains("this amount is haist than the rest amoun"));
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void createPayment_ShouldThrowException_WhenOrderNotFound() {
        // Arrange
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, 
            () -> paymentService.createPayment(999L, paymentRequest));
        verify(orderRepository, times(1)).findById(999L);
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void getPaymentsOfOrder_ShouldReturnPaymentsList() {
        // Arrange
        Payment payment2 = new Payment();
        payment2.setId(2L);
        payment2.setOrder(order);
        
        List<Payment> payments = Arrays.asList(payment, payment2);
        
        when(paymentRepository.findAll()).thenReturn(payments);
        when(paymentMapper.toDto(any(Payment.class))).thenReturn(paymentResponse);

        // Act
        List<PaymentResponse> result = paymentService.getPaymentsOfOrder(1L);

        // Assert
        assertNotNull(result);
        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    void updatePaymentStatus_ShouldUpdateStatus_WhenPaymentExists() {
        // Arrange
        PaymentStatusRequest statusRequest = new PaymentStatusRequest();
        statusRequest.setStatus("ENCAISSE");
        
        payment.setOrder(order);
        List<Payment> payments = Arrays.asList(payment);
        
        when(paymentRepository.findAll()).thenReturn(payments);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentMapper.toDto(payment)).thenReturn(paymentResponse);

        // Act
        PaymentResponse result = paymentService.updatePaymentStatus(1L, 1L, statusRequest);

        // Assert
        assertNotNull(result);
        assertEquals(PaymentStatus.ENCAISSE, payment.getStatus());
        assertNotNull(payment.getDateEncaissement());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void updatePaymentStatus_ShouldThrowException_WhenPaymentNotFound() {
        // Arrange
        PaymentStatusRequest statusRequest = new PaymentStatusRequest();
        statusRequest.setStatus("ENCAISSE");
        
        when(paymentRepository.findAll()).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(RuntimeException.class, 
            () -> paymentService.updatePaymentStatus(999L, 999L, statusRequest));
        verify(paymentRepository, times(1)).findAll();
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void createPayment_ShouldUpdateClientTotalDepense_WhenFullPayment() {
        // Arrange
        paymentRequest.setAmount(1000.0);
        payment.setAmount(1000.0);
        
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentMapper.toEntity(paymentRequest)).thenReturn(payment);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(paymentMapper.toDto(payment)).thenReturn(paymentResponse);
        when(clientService.changeFidelite(client)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(client);
        when(orderRepository.save(order)).thenReturn(order);

        // Act
        PaymentResponse result = paymentService.createPayment(1L, paymentRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1000.0, client.getTotalDepense());
        verify(clientRepository, times(1)).save(client);
    }
}

