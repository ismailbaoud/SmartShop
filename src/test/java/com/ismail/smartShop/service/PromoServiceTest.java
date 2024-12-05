package com.ismail.smartShop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ismail.smartShop.dto.promo.request.PromoRequest;
import com.ismail.smartShop.dto.promo.response.PromoResponse;
import com.ismail.smartShop.helper.CodeGenerater;
import com.ismail.smartShop.mapper.PromoMapper;
import com.ismail.smartShop.model.Promo;
import com.ismail.smartShop.repository.PromoRepository;
import com.ismail.smartShop.service.implementation.PromoServiceImpl;

@ExtendWith(MockitoExtension.class)
class PromoServiceTest {

    @Mock
    private PromoRepository promoRepository;

    @Mock
    private PromoMapper promoMapper;

    @Mock
    private CodeGenerater codeGenerater;

    @InjectMocks
    private PromoServiceImpl promoService;

    private Promo promo;
    private PromoRequest promoRequest;
    private PromoResponse promoResponse;

    @BeforeEach
    void setUp() {
        promo = new Promo();
        promo.setId(1L);
        promo.setCode("PROMO-TEST");
        promo.setDiscountPercent(10);
        promo.setUsedTimes(0);
        promo.setExpiresAt(LocalDateTime.now().plusDays(30));

        promoRequest = new PromoRequest();
        promoRequest.setDiscountPercent(10);
        promoRequest.setExpiresAt(LocalDateTime.now().plusDays(30));

        promoResponse = new PromoResponse(
            1L,
            "PROMO-TEST",
            10,
            "PROMO-TEST",
            LocalDateTime.now().plusDays(30),
            0
        );
    }

    @Test
    void createPromoCode_ShouldCreatePromo_WhenValidRequest() {
        // Arrange
        when(codeGenerater.generatePromoCode()).thenReturn("PROMO-TEST");
        when(promoMapper.toEntity(promoRequest)).thenReturn(promo);
        when(promoRepository.save(promo)).thenReturn(promo);
        when(promoMapper.toDto(promo)).thenReturn(promoResponse);

        // Act
        PromoResponse result = promoService.createPromoCode(promoRequest);

        // Assert
        assertNotNull(result);
        assertEquals("PROMO-TEST", result.code());
        assertEquals(10, result.discountPercent());
        verify(codeGenerater, times(1)).generatePromoCode();
        verify(promoRepository, times(1)).save(promo);
    }

    @Test
    void validatePromoCode_ShouldReturnTrue_WhenPromoExistsAndNotExpired() {
        // Arrange
        promo.setExpiresAt(LocalDateTime.now().plusDays(10));
        when(promoRepository.findPromoByCode("PROMO-TEST")).thenReturn(promo);

        // Act
        Boolean result = promoService.validatePromoCode("PROMO-TEST");

        // Assert
        assertTrue(result);
        verify(promoRepository, times(1)).findPromoByCode("PROMO-TEST");
    }

    @Test
    void validatePromoCode_ShouldReturnFalse_WhenPromoExpired() {
        // Arrange
        promo.setExpiresAt(LocalDateTime.now().minusDays(1));
        when(promoRepository.findPromoByCode("PROMO-EXPIRED")).thenReturn(promo);

        // Act
        Boolean result = promoService.validatePromoCode("PROMO-EXPIRED");

        // Assert
        assertFalse(result);
        verify(promoRepository, times(1)).findPromoByCode("PROMO-EXPIRED");
    }

    @Test
    void validatePromoCode_ShouldReturnFalse_WhenPromoNotFound() {
        // Arrange
        when(promoRepository.findPromoByCode("INVALID-CODE")).thenReturn(null);

        // Act
        Boolean result = promoService.validatePromoCode("INVALID-CODE");

        // Assert
        assertFalse(result);
        verify(promoRepository, times(1)).findPromoByCode("INVALID-CODE");
    }

    @Test
    void getPromoByCode_ShouldReturnPromo_WhenPromoExists() {
        // Arrange
        when(promoRepository.findPromoByCode("PROMO-TEST")).thenReturn(promo);
        when(promoMapper.toDto(promo)).thenReturn(promoResponse);

        // Act
        PromoResponse result = promoService.getPromoByCode("PROMO-TEST");

        // Assert
        assertNotNull(result);
        assertEquals("PROMO-TEST", result.code());
        assertEquals(10, result.discountPercent());
        verify(promoRepository, times(1)).findPromoByCode("PROMO-TEST");
    }

    @Test
    void validatePromoCode_ShouldReturnTrue_WhenPromoExpiresInFuture() {
        // Arrange
        promo.setExpiresAt(LocalDateTime.now().plusYears(1));
        when(promoRepository.findPromoByCode("PROMO-FUTURE")).thenReturn(promo);

        // Act
        Boolean result = promoService.validatePromoCode("PROMO-FUTURE");

        // Assert
        assertTrue(result);
        verify(promoRepository, times(1)).findPromoByCode("PROMO-FUTURE");
    }
}

