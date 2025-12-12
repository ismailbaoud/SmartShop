package com.ismail.smartShop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ismail.smartShop.dto.client.response.ClientResponse;
import com.ismail.smartShop.dto.order.request.OrderRequest;
import com.ismail.smartShop.dto.order.response.OrderResponse;
import com.ismail.smartShop.dto.orderItem.Request.OrderItemRequest;
import com.ismail.smartShop.dto.product.response.ProductResponse;
import com.ismail.smartShop.mapper.ClientMapper;
import com.ismail.smartShop.mapper.OrderMapper;
import com.ismail.smartShop.model.Client;
import com.ismail.smartShop.model.Order;
import com.ismail.smartShop.model.OrderItem;
import com.ismail.smartShop.model.Product;
import com.ismail.smartShop.model.enums.NiveauFidelite;
import com.ismail.smartShop.model.enums.OrderStatus;
import com.ismail.smartShop.repository.ClientRepository;
import com.ismail.smartShop.repository.OrderRepository;
import com.ismail.smartShop.service.implementation.ClientServiceImpl;
import com.ismail.smartShop.service.implementation.OrderItemServiceImpl;
import com.ismail.smartShop.service.implementation.OrderServiceImpl;
import com.ismail.smartShop.service.implementation.ProductServiceImpl;
import com.ismail.smartShop.service.implementation.PromoServiceImpl;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private ClientServiceImpl clientService;

    @Mock
    private PromoServiceImpl promoService;

    @Mock
    private ProductServiceImpl productService;

    @Mock
    private OrderItemServiceImpl orderItemService;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private OrderRequest orderRequest;
    private OrderResponse orderResponse;
    private Client client;
    private ClientResponse clientResponse;
    private Product product;
    private ProductResponse productResponse;
    private OrderItemRequest orderItemRequest;

    @BeforeEach
    void setUp() {
        client = Client.builder()
            .id(1L)
            .nom("Test Client")
            .email("client@test.com")
            .niveauDeFidelite(NiveauFidelite.BASIC)
            .totalCommandes(0)
            .totalDepense(0.0)
            .build();

        clientResponse = new ClientResponse(
            1L,
            "Test Client",
            "client@test.com",
            0,
            0.0,
            NiveauFidelite.BASIC,
            null,
            null
        );

        product = Product.builder()
            .id(1L)
            .nom("Test Product")
            .prixUnit(100.0)
            .stockQuantitie(50)
            .build();

        productResponse = new ProductResponse(
            1L,
            "Test Product",
            100.0,
            50,
            LocalDateTime.now(),
            null,
            null
        );

        orderItemRequest = new OrderItemRequest();
        orderItemRequest.setProduct_id(1L);
        orderItemRequest.setQuantite(5);

        orderRequest = new OrderRequest();
        orderRequest.setClient_id(1L);
        orderRequest.setPromo("PROMO-TEST");
        orderRequest.setTva(20.0);
        orderRequest.setItems(List.of(orderItemRequest));

        order = new Order();
        order.setId(1L);
        order.setClient(client);
        order.setDateOrder(LocalDateTime.now());
        order.setSubTotal(500.0);
        order.setTotalTTC(600.0);
        order.setMontant_restant(600.0);
        order.setStatus(OrderStatus.PANDING);
        order.setPromo("PROMO-TEST");

        orderResponse = new OrderResponse(
            1L,
            "PROMO-TEST",
            client,
            new ArrayList<>(),
            LocalDateTime.now(),
            500.0,
            null,
            20.0,
            600.0,
            OrderStatus.PANDING,
            600.0, null
        );
    }

    @Test
    void createOrder_ShouldCreateOrder_WhenValidRequestWithoutPromo() {
        // Arrange
        orderRequest.setPromo(null);
        when(clientService.getClientById(1L)).thenReturn(clientResponse);
        when(promoService.validatePromoCode(null)).thenReturn(false);
        when(productService.getProductById(1L)).thenReturn(productResponse);
        when(orderMapper.toEntity(orderRequest)).thenReturn(order);
        when(clientMapper.fromResponse(clientResponse)).thenReturn(client);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderResponse);
        when(productService.discountProducts(1L, 5)).thenReturn(1);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(client)).thenReturn(client);

        // Act
        OrderResponse result = orderService.createOrder(orderRequest);

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatus.PANDING, result.status());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void createOrder_ShouldApplyPromoDiscount_WhenValidPromoCode() {
        // Arrange
        com.ismail.smartShop.dto.promo.response.PromoResponse promoResponse =
            new com.ismail.smartShop.dto.promo.response.PromoResponse(
                1L, 10, "PROMO-TEST", LocalDateTime.now().plusDays(30), 0
            );

        when(clientService.getClientById(1L)).thenReturn(clientResponse);
        when(promoService.validatePromoCode("PROMO-TEST")).thenReturn(true);
        when(promoService.getPromoByCode("PROMO-TEST")).thenReturn(promoResponse);
        when(productService.getProductById(1L)).thenReturn(productResponse);
        when(orderMapper.toEntity(orderRequest)).thenReturn(order);
        when(clientMapper.fromResponse(clientResponse)).thenReturn(client);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderResponse);
        when(productService.discountProducts(1L, 5)).thenReturn(1);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(client)).thenReturn(client);

        // Act
        OrderResponse result = orderService.createOrder(orderRequest);

        // Assert
        assertNotNull(result);
        verify(promoService, times(1)).validatePromoCode("PROMO-TEST");
        verify(promoService, times(1)).getPromoByCode("PROMO-TEST");
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_ShouldSetFirstOrderDate_WhenFirstOrder() {
        // Arrange
        client.setFirstOrderDate(null);
        when(clientService.getClientById(1L)).thenReturn(clientResponse);
        when(promoService.validatePromoCode(anyString())).thenReturn(false);
        when(productService.getProductById(1L)).thenReturn(productResponse);
        when(orderMapper.toEntity(orderRequest)).thenReturn(order);
        when(clientMapper.fromResponse(clientResponse)).thenReturn(client);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderResponse);
        when(productService.discountProducts(1L, 5)).thenReturn(1);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(client)).thenReturn(client);

        // Act
        OrderResponse result = orderService.createOrder(orderRequest);

        // Assert
        assertNotNull(result);
        assertNotNull(client.getFirstOrderDate());
        assertNotNull(client.getLastOrderDate());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void createOrder_ShouldUseDefaultTVA_WhenTVANotProvided() {
        // Arrange
        orderRequest.setTva(null);
        when(clientService.getClientById(1L)).thenReturn(clientResponse);
        when(promoService.validatePromoCode(anyString())).thenReturn(false);
        when(productService.getProductById(1L)).thenReturn(productResponse);
        when(orderMapper.toEntity(orderRequest)).thenReturn(order);
        when(clientMapper.fromResponse(clientResponse)).thenReturn(client);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderResponse);
        when(productService.discountProducts(1L, 5)).thenReturn(1);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(client)).thenReturn(client);

        // Act
        OrderResponse result = orderService.createOrder(orderRequest);

        // Assert
        assertNotNull(result);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void getAllOrders_ShouldReturnAllOrders() {
        // Arrange
        Order order2 = new Order();
        order2.setId(2L);
        List<Order> orders = Arrays.asList(order, order2);

        when(orderRepository.findAll()).thenReturn(orders);
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderResponse);

        // Act
        List<OrderResponse> result = orderService.getAllOrders();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void getAllOrdersOfClient_ShouldReturnClientOrders() {
        // Arrange
        List<Order> orders = List.of(order);

        when(orderRepository.findAll()).thenReturn(orders);
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderResponse);

        // Act
        List<OrderResponse> result = orderService.getAllOrdersOfClient(1L);

        // Assert
        assertNotNull(result);
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void cancelOrder_ShouldCancelOrderAndRestoreStock() {
        // Arrange
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantitie(5);
        order.setOrderItems(List.of(orderItem));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderResponse);
        when(productService.addProducts(1L, 5)).thenReturn(1);

        // Act
        OrderResponse result = orderService.cancelOrder(1L);

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatus.CANCELED, order.getStatus());
        verify(orderRepository, times(1)).save(order);
        verify(productService, times(1)).addProducts(1L, 5);
    }

    @Test
    void cancelOrder_ShouldThrowException_WhenOrderNotFound() {
        // Arrange
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.cancelOrder(999L));
        verify(orderRepository, times(1)).findById(999L);
    }

    @Test
    void confirmOrder_ShouldConfirmOrder_WhenOrderExists() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderResponse);

        // Act
        OrderResponse result = orderService.confirmOrder(1L);

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void confirmOrder_ShouldThrowException_WhenOrderNotFound() {
        // Arrange
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.confirmOrder(999L));
        verify(orderRepository, times(1)).findById(999L);
    }
}
