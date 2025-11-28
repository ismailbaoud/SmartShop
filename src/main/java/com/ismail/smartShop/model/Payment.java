package com.ismail.smartShop.model;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.boot.autoconfigure.web.WebProperties.Resources.Chain.Strategy;

import com.ismail.smartShop.model.enums.PaymentMethod;
import com.ismail.smartShop.model.enums.PaymentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroPaiement;

    private Double amount;
    
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    private String banque;

    private LocalDateTime datePaiement;

    private LocalDateTime dateEncaissement;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;


}
