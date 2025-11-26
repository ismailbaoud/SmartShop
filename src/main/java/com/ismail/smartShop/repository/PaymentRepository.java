package com.ismail.smartShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ismail.smartShop.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment , Long> {
    
}
