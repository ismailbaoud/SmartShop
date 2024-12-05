package com.ismail.smartShop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ismail.smartShop.dto.orderItem.Request.OrderItemRequest;
import com.ismail.smartShop.dto.orderItem.response.OrderItemResponse;
import com.ismail.smartShop.exception.product.ProductNotFoundException;
import com.ismail.smartShop.mapper.OrderItemMapper;
import com.ismail.smartShop.model.Order;
import com.ismail.smartShop.model.OrderItem;
import com.ismail.smartShop.model.Product;
import com.ismail.smartShop.repository.OrderItemRepository;
import com.ismail.smartShop.repository.ProductRepository;
import com.ismail.smartShop.service.implementation.OrderItemServiceImpl;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderItemMapper orderItemMapper;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    private OrderItem orderItem;
    private OrderItemRequest orderItemRequest;
    private OrderItemResponse orderItemResponse;
    private Product product;
    private Order order;

    @BeforeEach
    void setUp() {
        product = Product.builder()
            .id(1L)
            .nom("Test Product")
            .prixUnit(100.0)
            .stockQuantitie(50)
            .build();

        order = new Order();
        order.setId(1L);

        orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setProduct(product);
        orderItem.setQuantitie(5);
        orderItem.setPrixUnitaire(100.0);
        orderItem.setLineTotal(500.0);
        orderItem.setOrder(order);

        orderItemRequest = new OrderItemRequest();
        orderItemRequest.setProduct_id(1L);
        orderItemRequest.setQuantite(5);

        orderItemResponse = new OrderItemResponse(
            product,
            5,
            100.0,
            500.0,
            null
        );
    }

    @Test
    void createOrderItem_ShouldCreateOrderItem_WhenValidRequest() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderItemMapper.toEntity(orderItemRequest)).thenReturn(orderItem);
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);
        when(orderItemMapper.toDto(orderItem)).thenReturn(orderItemResponse);

        // Act
        OrderItemResponse result = orderItemService.createOrderItem(orderItemRequest, order);

        // Assert
        assertNotNull(result);
        assertEquals(5, result.quantite());
        assertEquals(100.0, result.prixUnitaire());
        assertEquals(500.0, result.linkTotal());
        verify(productRepository, times(1)).findById(1L);
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }

    @Test
    void createOrderItem_ShouldThrowException_WhenProductNotFound() {
        // Arrange
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        orderItemRequest.setProduct_id(999L);

        // Act & Assert
        assertThrows(ProductNotFoundException.class, 
            () -> orderItemService.createOrderItem(orderItemRequest, order));
        verify(productRepository, times(1)).findById(999L);
        verify(orderItemRepository, never()).save(any(OrderItem.class));
    }

    @Test
    void getAllOrderItem_ShouldReturnListOfOrderItems() {
        // Arrange
        OrderItem orderItem2 = new OrderItem();
        orderItem2.setId(2L);
        
        List<OrderItem> orderItems = Arrays.asList(orderItem, orderItem2);
        
        when(orderItemRepository.findAll()).thenReturn(orderItems);
        when(orderItemMapper.toDto(any(OrderItem.class))).thenReturn(orderItemResponse);

        // Act
        List<OrderItemResponse> result = orderItemService.getAllOrderItem();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderItemRepository, times(1)).findAll();
    }

    @Test
    void createOrderItem_ShouldCalculateLineTotalCorrectly() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderItemMapper.toEntity(orderItemRequest)).thenReturn(orderItem);
        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(invocation -> {
            OrderItem saved = invocation.getArgument(0);
            assertEquals(500.0, saved.getLineTotal());
            return saved;
        });
        when(orderItemMapper.toDto(any(OrderItem.class))).thenReturn(orderItemResponse);

        // Act
        OrderItemResponse result = orderItemService.createOrderItem(orderItemRequest, order);

        // Assert
        assertNotNull(result);
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }
}

