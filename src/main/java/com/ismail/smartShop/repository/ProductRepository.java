package com.ismail.smartShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ismail.smartShop.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
