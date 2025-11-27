package com.ismail.smartShop.dto.promo.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromoRequest {
    private Integer discountPercent;
    private LocalDateTime expiresAt;
}
