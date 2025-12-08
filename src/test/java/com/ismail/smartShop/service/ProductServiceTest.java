package com.ismail.smartShop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ismail.smartShop.dto.product.request.ProductRequest;
import com.ismail.smartShop.dto.product.response.ProductResponse;
import com.ismail.smartShop.exception.product.ProductNotFoundException;
import com.ismail.smartShop.exception.product.QuantityNotAvailableException;
import com.ismail.smartShop.mapper.ProductMapper;
import com.ismail.smartShop.model.Product;
import com.ismail.smartShop.repository.ProductRepository;
import com.ismail.smartShop.service.implementation.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductRequest productRequest;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        product = Product.builder()
            .id(1L)
            .nom("Product 1")
            .prixUnit(100.0)
            .stockQuantitie(50)
            .createdAt(LocalDateTime.now())
            .build();

        productRequest = ProductRequest.builder()
            .nom("Product 1")
            .prixUnit(100.0)
            .stockQuantitie(50)
            .build();

        productResponse = new ProductResponse(
            1L,
            "Product 1",
            100.0,
            50,
            LocalDateTime.now(),
            null,
            null
        );
    }

    @Test
    void createProduct_ShouldCreateProduct_WhenValidRequest() {
        // Arrange
        when(productMapper.toEntity(productRequest)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productResponse);

        // Act
        ProductResponse result = productService.createProduct(productRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Product 1", result.nom());
        assertEquals(100.0, result.prixUnit());
        assertEquals(50, result.stockQuantitie());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getProductById_ShouldReturnProduct_WhenProductExists() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productResponse);

        // Act
        ProductResponse result = productService.getProductById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getProductById_ShouldThrowException_WhenProductNotFound() {
        // Arrange
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(999L));
        verify(productRepository, times(1)).findById(999L);
    }

    @Test
    void updateProduct_ShouldUpdateProduct_WhenProductExists() {
        // Arrange
        ProductRequest updateRequest = ProductRequest.builder()
            .nom("Updated Product")
            .prixUnit(150.0)
            .stockQuantitie(100)
            .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productResponse);

        // Act
        ProductResponse result = productService.updateProduct(1L, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Product", product.getNom());
        assertEquals(150.0, product.getPrixUnit());
        assertEquals(100, product.getStockQuantitie());
        assertNotNull(product.getUpdatedAt());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void updateProduct_ShouldThrowException_WhenProductNotFound() {
        // Arrange
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, 
            () -> productService.updateProduct(999L, productRequest));
    }

    @Test
    void deleteProduct_ShouldDeleteProduct() {
        // Arrange
        doNothing().when(productRepository).deleteById(1L);

        // Act
        productService.deleteProduct(1L);

        // Assert
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void getAllProducts_ShouldReturnListOfProducts() {
        // Arrange
        Product product2 = Product.builder()
            .id(2L)
            .nom("Product 2")
            .prixUnit(200.0)
            .stockQuantitie(30)
            .build();

        List<Product> products = Arrays.asList(product, product2);
        
        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toDto(any(Product.class))).thenReturn(productResponse);

        // Act
        List<ProductResponse> result = productService.getAllProducts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void discountProducts_ShouldDecreaseStock_WhenQuantityAvailable() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.discountStock(1L, 10)).thenReturn(1);

        // Act
        Integer result = productService.discountProducts(1L, 10);

        // Assert
        assertEquals(1, result);
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).discountStock(1L, 10);
    }

    @Test
    void discountProducts_ShouldThrowException_WhenQuantityNotAvailable() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act & Assert
        assertThrows(QuantityNotAvailableException.class, 
            () -> productService.discountProducts(1L, 100));
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, never()).discountStock(anyLong(), anyInt());
    }

    @Test
    void addProducts_ShouldIncreaseStock() {
        // Arrange
        when(productRepository.addStock(1L, 20)).thenReturn(1);

        // Act
        Integer result = productService.addProducts(1L, 20);

        // Assert
        assertEquals(1, result);
        verify(productRepository, times(1)).addStock(1L, 20);
    }
}

