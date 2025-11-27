package com.ismail.smartShop.service.implementation;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.ismail.smartShop.dto.promo.request.PromoRequest;
import com.ismail.smartShop.dto.promo.response.PromoResponse;
import com.ismail.smartShop.helper.CodeGenerater;
import com.ismail.smartShop.mapper.PromoMapper;
import com.ismail.smartShop.model.Promo;
import com.ismail.smartShop.repository.PromoRepository;
import com.ismail.smartShop.service.PromoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromoServiceImpl implements PromoService {

    private final PromoRepository promoRepository;
    private final PromoMapper promoMapper;
    private final CodeGenerater codeGenerater;
    
    @Override
    public PromoResponse createPromoCode(PromoRequest pReq) {
        String code = codeGenerater.generatePromoCode();
        Promo p = promoMapper.toEntity(pReq);
        p.setCode(code);
        return promoMapper.toDto(promoRepository.save(p));
    }

    @Override
    public Boolean validatePromoCode(String code) {
        Promo p = promoRepository.findPromoByCode(code);
        if(p != null) {
            return p.getExpiresAt().isAfter(LocalDateTime.now()) ? true : false;
        }
        return false;
    }

    @Override
    public PromoResponse getPromoByCode(String code) {
        return promoMapper.toDto(promoRepository.findPromoByCode(code));
    }
    
}
