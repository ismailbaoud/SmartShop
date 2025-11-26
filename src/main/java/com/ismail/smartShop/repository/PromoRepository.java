package com.ismail.smartShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ismail.smartShop.model.Promo;

public interface PromoRepository extends JpaRepository<Promo, Long> {
    
}
