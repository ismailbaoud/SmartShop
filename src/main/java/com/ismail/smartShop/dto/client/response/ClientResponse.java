package com.ismail.smartShop.dto.client.response;

import java.time.LocalDateTime;

import com.ismail.smartShop.model.enums.NiveauFidelite;

public record ClientResponse(
    Long id,
    String nom,
    String email,

    Integer totalCommandes,
    Double totalDepense,

    NiveauFidelite niveauDeFidelite,

    LocalDateTime firstOrderDate,
    LocalDateTime lastOrderDate
){}
