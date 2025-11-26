package com.ismail.smartShop.dto.product.response;

import java.time.LocalDateTime;

public record ProductResponse(
    Long id,
    String nom,
    Double prixUnit,
    Integer stockQuantitie,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime deledAt
)
{}
