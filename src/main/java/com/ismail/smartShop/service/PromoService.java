package com.ismail.smartShop.service;

import com.ismail.smartShop.dto.promo.request.PromoRequest;
import com.ismail.smartShop.dto.promo.response.PromoResponse;

public interface PromoService {
    public PromoResponse createPromoCode(PromoRequest pReq);
    public Boolean validatePromoCode(String code);
    public PromoResponse getPromoByCode(String code);
}
