package com.ismail.smartShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ismail.smartShop.model.Product;

import jakarta.transaction.Transactional;

public interface ProductRepository extends JpaRepository<Product, Long> {

@Modifying
@Transactional
@Query("UPDATE Product p SET p.stockQuantitie = p.stockQuantitie + :q WHERE p.id = :id")
Integer addStock(Long id, Integer q);


@Modifying
@Transactional
@Query("UPDATE Product p SET p.stockQuantitie = p.stockQuantitie - :q WHERE p.id = :id")
Integer discountStock(Long id, Integer q);
}
