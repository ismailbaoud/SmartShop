package com.ismail.smartShop.dto.promo.response;

import java.time.LocalDateTime;

public record PromoResponse(
        Long id,
        String PromoCode,
        Integer discountPercent,
        String code,
        LocalDateTime expiresAt,
        Integer usedTimes
) {}
