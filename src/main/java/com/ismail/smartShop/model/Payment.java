package com.ismail.smartShop.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.boot.autoconfigure.web.WebProperties.Resources.Chain.Strategy;

import jakarta.persistence.Entity;
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

    private BigDecimal Amount;

    private String method;

    private String reference;

    private String banque;

    private LocalDateTime datePaiement;

    private LocalDateTime dateEncaissement;

    private String status;


    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;


}
