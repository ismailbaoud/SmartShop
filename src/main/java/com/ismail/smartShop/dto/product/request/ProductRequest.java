package com.ismail.smartShop.dto.product.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {
    @NotBlank(message = "name is required")
    private String nom;

    @NotNull(message = "unit price is required")
    @Positive
    private Double prixUnit;

    @NotNull(message = "stock quantity is required")
    @Positive
    private Integer stockQuantitie;
}
