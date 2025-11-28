package com.ismail.smartShop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismail.smartShop.dto.promo.request.PromoRequest;
import com.ismail.smartShop.dto.promo.response.PromoResponse;
import com.ismail.smartShop.service.implementation.PromoServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/api/promocodes")
@RequiredArgsConstructor
public class promoController {

    private final PromoServiceImpl promoService;

    @PostMapping
    public ResponseEntity<PromoResponse> createPromo(@Valid @RequestBody PromoRequest promoReq) {
        return ResponseEntity.ok().body(promoService.createPromoCode(promoReq));
    }

    @GetMapping
    public ResponseEntity<PromoResponse> getPromoByCode(@RequestBody String code) {
        return ResponseEntity.ok().body(promoService.getPromoByCode(code));
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validatePromoCode(@RequestBody String code) {
        return ResponseEntity.ok().body(promoService.validatePromoCode(code));
    }
    
}
