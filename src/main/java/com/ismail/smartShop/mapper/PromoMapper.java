package com.ismail.smartShop.mapper;

import org.mapstruct.Mapper;

import com.ismail.smartShop.dto.promo.request.PromoRequest;
import com.ismail.smartShop.dto.promo.response.PromoResponse;
import com.ismail.smartShop.model.Promo;

@Mapper
public interface PromoMapper {
    Promo toEntity(PromoRequest req);
    PromoResponse toDto(Promo p);
}
