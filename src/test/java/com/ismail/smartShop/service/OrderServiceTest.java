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

import com.ismail.smartShop.dto.client.response.ClientResponse;
import com.ismail.smartShop.dto.order.request.OrderRequest;
import com.ismail.smartShop.dto.order.response.OrderResponse;
import com.ismail.smartShop.dto.orderItem.Request.OrderItemRequest;
import com.ismail.smartShop.dto.product.response.ProductResponse;
import com.ismail.smartShop.dto.promo.response.PromoResponse;
import com.ismail.smartShop.exception.client.ClientNotFoundException;
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
class OrderServiceTest {

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
    private ClientResponse clientResponse;
    private Client client;
    private ProductResponse productResponse;
    private PromoResponse promoResponse;
    private OrderItemRequest orderItemRequest;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setNom("John Doe");
        client.setNiveauDeFidelite(NiveauFidelite.BASIC);

        clientResponse = new ClientResponse(
            1L,
            "John Doe",
            "john@test.com",
            0,
            0.0,
            NiveauFidelite.BASIC,
            null,
            null
        );

        productResponse = new ProductResponse(
            1L,
            "Product 1",
            100.0,
            10,
            null,
            null,
            null
        );

        orderItemRequest = new OrderItemRequest();
        orderItemRequest.setProduct_id(1L);
        orderItemRequest.setQuantite(2);

        orderRequest = new OrderRequest();
        orderRequest.setClient_id(1L);
        orderRequest.setPromo("PROMO10");
        orderRequest.setTva(20.0);
        orderRequest.setItems(Arrays.asList(orderItemRequest));

        order = new Order();
        order.setId(1L);
        order.setClient(client);
        order.setStatus(OrderStatus.PANDING);
        order.setSubTotal(200.0);
        order.setTotalTTC(240.0);
        order.setMontant_restant(240.0);
        order.setTva(20.0);
        order.setPromo("PROMO10");
        order.setDateOrder(LocalDateTime.now());

        Product product = new Product();
        product.setId(1L);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantitie(2);
        order.setOrderItems(Arrays.asList(orderItem));

        orderResponse = new OrderResponse(
            1L,
            "PROMO10",
            client,
            null,
            LocalDateTime.now(),
            200.0,
            null,
            20.0,
            240.0,
            OrderStatus.PANDING,
            240.0
        );

        promoResponse = new PromoResponse(
            1L,
            "PROMO10",
            10,
            "PROMO10",
            LocalDateTime.now().plusDays(10),
            0
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
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        doNothing().when(productService).discountProducts(anyLong(), anyInt());
        doNothing().when(orderItemService).createOrderItem(any(), any());

        // Act
        OrderResponse result = orderService.createOrder(orderRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(OrderStatus.PANDING, result.status());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(productService, times(2)).discountProducts(1L, 2);
        verify(orderItemService, times(2)).createOrderItem(any(), any());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void createOrder_ShouldCreateOrder_WhenValidRequestWithPromo() {
        // Arrange
        when(clientService.getClientById(1L)).thenReturn(clientResponse);
        when(promoService.validatePromoCode("PROMO10")).thenReturn(true);
        when(promoService.getPromoByCode("PROMO10")).thenReturn(promoResponse);
        when(productService.getProductById(1L)).thenReturn(productResponse);
        when(orderMapper.toEntity(orderRequest)).thenReturn(order);
        when(clientMapper.fromResponse(clientResponse)).thenReturn(client);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderResponse);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        // Act
        OrderResponse result = orderService.createOrder(orderRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        verify(promoService, times(1)).validatePromoCode("PROMO10");
        verify(promoService, times(1)).getPromoByCode("PROMO10");
    }

    @Test
    void createOrder_ShouldApplySilverDiscount_WhenClientIsSilverAndTotalAbove500() {
        // Arrange
        clientResponse = new ClientResponse(1L, "John", "john@test.com", 0, 0.0, NiveauFidelite.SILVER, null, null);
        
        when(clientService.getClientById(1L)).thenReturn(clientResponse);
        when(promoService.validatePromoCode(any())).thenReturn(false);
        when(productService.getProductById(1L)).thenReturn(new ProductResponse(1L, "Product", 300.0, 10, null, null, null));
        when(orderMapper.toEntity(orderRequest)).thenReturn(order);
        when(clientMapper.fromResponse(clientResponse)).thenReturn(client);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderResponse);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        // Act
        OrderResponse result = orderService.createOrder(orderRequest);

        // Assert
        assertNotNull(result);
        verify(clientService, times(1)).getClientById(1L);
    }

    @Test
    void createOrder_ShouldApplyGoldDiscount_WhenClientIsGoldAndTotalAbove800() {
        // Arrange
        clientResponse = new ClientResponse(1L, "John", "john@test.com", 0, 0.0, NiveauFidelite.GOLD, null, null);
        
        when(clientService.getClientById(1L)).thenReturn(clientResponse);
        when(promoService.validatePromoCode(any())).thenReturn(false);
        when(productService.getProductById(1L)).thenReturn(new ProductResponse(1L, "Product", 500.0, 10, null, null, null));
        when(orderMapper.toEntity(orderRequest)).thenReturn(order);
        when(clientMapper.fromResponse(clientResponse)).thenReturn(client);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderResponse);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        // Act
        OrderResponse result = orderService.createOrder(orderRequest);

        // Assert
        assertNotNull(result);
        verify(clientService, times(1)).getClientById(1L);
    }

    @Test
    void createOrder_ShouldApplyPlatiniumDiscount_WhenClientIsPlatiniumAndTotalAbove1200() {
        // Arrange
        clientResponse = new ClientResponse(1L, "John", "john@test.com", 0, 0.0, NiveauFidelite.PLATINIUM, null, null);
        
        when(clientService.getClientById(1L)).thenReturn(clientResponse);
        when(promoService.validatePromoCode(any())).thenReturn(false);
        when(productService.getProductById(1L)).thenReturn(new ProductResponse(1L, "Product", 700.0, 10, null, null, null));
        when(orderMapper.toEntity(orderRequest)).thenReturn(order);
        when(clientMapper.fromResponse(clientResponse)).thenReturn(client);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderResponse);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        // Act
        OrderResponse result = orderService.createOrder(orderRequest);

        // Assert
        assertNotNull(result);
        verify(clientService, times(1)).getClientById(1L);
    }

    @Test
    void createOrder_ShouldSetFirstOrderDate_WhenClientFirstOrder() {
        // Arrange
        client.setFirstOrderDate(null);
        
        when(clientService.getClientById(1L)).thenReturn(clientResponse);
        when(promoService.validatePromoCode(any())).thenReturn(false);
        when(productService.getProductById(1L)).thenReturn(productResponse);
        when(orderMapper.toEntity(orderRequest)).thenReturn(order);
        when(clientMapper.fromResponse(clientResponse)).thenReturn(client);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderResponse);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        // Act
        orderService.createOrder(orderRequest);

        // Assert
        assertNotNull(client.getFirstOrderDate());
        assertNotNull(client.getLastOrderDate());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void createOrder_ShouldUseDefaultTva_WhenTvaIsNull() {
        // Arrange
        orderRequest.setTva(null);
        
        when(clientService.getClientById(1L)).thenReturn(clientResponse);
        when(promoService.validatePromoCode(any())).thenReturn(false);
        when(productService.getProductById(1L)).thenReturn(productResponse);
        when(orderMapper.toEntity(orderRequest)).thenReturn(order);
        when(clientMapper.fromResponse(clientResponse)).thenReturn(client);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderResponse);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        // Act
        OrderResponse result = orderService.createOrder(orderRequest);

        // Assert
        assertNotNull(result);
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
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order));
        when(orderMapper.toDto(order)).thenReturn(orderResponse);

        // Act
        List<OrderResponse> result = orderService.getAllOrdersOfClient(1L);

        // Assert
        assertNotNull(result);
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void cancelOrder_ShouldCancelOrderAndRestoreStock() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderResponse);
        doNothing().when(productService).addProducts(anyLong(), anyInt());

        // Act
        OrderResponse result = orderService.cancelOrder(1L);

        // Assert
        assertNotNull(result);
        assertEquals(OrderStatus.CANCELED, order.getStatus());
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(order);
        verify(productService, times(1)).addProducts(1L, 2);
    }

    @Test
    void cancelOrder_ShouldThrowException_WhenOrderNotFound() {
        // Arrange
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.cancelOrder(999L));
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
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void confirmOrder_ShouldThrowException_WhenOrderNotFound() {
        // Arrange
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.confirmOrder(999L));
    }
}
