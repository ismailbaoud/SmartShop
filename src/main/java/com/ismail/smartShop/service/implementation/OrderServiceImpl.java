package com.ismail.smartShop.service.implementation;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

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
import com.ismail.smartShop.model.enums.NiveauFidelite;
import com.ismail.smartShop.model.enums.OrderStatus;
import com.ismail.smartShop.repository.ClientRepository;
import com.ismail.smartShop.repository.OrderRepository;
import com.ismail.smartShop.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ClientMapper clientMapper;
    private final ClientServiceImpl clientService;
    private final PromoServiceImpl promoService;
    private final ProductServiceImpl productService;
    private final OrderItemServiceImpl orderItemService;
    private final ClientRepository clientRepository;

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        ClientResponse client = clientService.getClientById(orderRequest.getClient_id());
        Boolean promoValid = promoService.validatePromoCode(orderRequest.getPromo());
        Double initTotal = orderRequest.getItems().stream()
        .mapToDouble(item -> {
        ProductResponse product = productService.getProductById(item.getProduct_id());
        return product.prixUnit() * item.getQuantite();
        })
        .sum();        
        Double htAfterFidelite = totalAfterFideliteHandle(client, initTotal);
        Double htAfterPromo = promoValid ? totalAfterCodePromo(htAfterFidelite, orderRequest.getPromo()) : htAfterFidelite;
        Double tvaPercent = orderRequest.getTva() != null ? orderRequest.getTva() : 20.0;
        Double totalTTC = htAfterPromo * (1 + tvaPercent / 100);
        

        Order order = orderMapper.toEntity(orderRequest);
        order.setClient(clientMapper.fromResponse(client));
        order.setDateOrder(LocalDateTime.now());
        order.setMontant_restant(totalTTC);
        if (promoValid) {
            order.setPromo(orderRequest.getPromo());
        } else {
            order.setPromo("NUN");
        }
        order.setStatus(OrderStatus.PANDING);
        order.setSubTotal(initTotal);
        order.setTotalTTC(totalTTC);
        order.setTva(tvaPercent);

        OrderResponse orderRes = orderMapper.toDto(orderRepository.save(order));
    for (OrderItemRequest item : orderRequest.getItems()) {

        productService.discountProducts(item.getProduct_id(), item.getQuantite());

        orderItemService.createOrderItem(item, order);
    }
        
        orderRequest.getItems().forEach(item -> 
            productService.discountProducts(item.getProduct_id(), item.getQuantite())
        );

        orderRequest.getItems().forEach(item -> 
            orderItemService.createOrderItem(item, order)
        );


        Client clientRes = clientRepository.findById(client.id()).orElseThrow(()-> new ClientNotFoundException());
        if(clientRes.getFirstOrderDate() == null) {
            clientRes.setFirstOrderDate(LocalDateTime.now());
        }
        clientRes.setLastOrderDate(LocalDateTime.now());
        clientRepository.save(clientRes);
        return orderRes;
    }

    private Double totalAfterFideliteHandle(ClientResponse client , Double total) {
        NiveauFidelite fidelite = client.niveauDeFidelite();
        Double newTotal = 0.0;
        if (fidelite.equals(NiveauFidelite.SILVER)) newTotal = total >= 500 ? total - (total * 5/100) : total;
        else if(fidelite.equals(NiveauFidelite.GOLD)) newTotal = total >= 800 ? total - (total * 10/100) : total;
        else if(fidelite.equals(NiveauFidelite.PLATINIUM)) newTotal = total >= 1200 ? total - (total * 15/100): total;
        else newTotal = total;
        return newTotal;
    }



    private Double totalAfterCodePromo(Double total ,String promoCode) {
        PromoResponse promo = promoService.getPromoByCode(promoCode);
        Double newTotal = total - (total * promo.discountPercent()/100);
        return newTotal;
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
        .map(a -> orderMapper.toDto(a)).toList();
    }

    @Override
    public List<OrderResponse> getAllOrdersOfClient(Long id) {
        return orderRepository.findAll().stream()
        .filter(a -> a.getId() == id)
        .map(a -> orderMapper.toDto(a)).toList();
    }

    @Override
    public OrderResponse cancelOrder(Long id) {
        Order order = orderRepository.findById(id) .orElseThrow(()-> new RuntimeException());
        order.setStatus(OrderStatus.CANCELED);
        OrderResponse or = orderMapper.toDto(orderRepository.save(order));
        order.getOrderItems().forEach(item -> {
        Long productId = item.getProduct().getId();
        Integer quantity = item.getQuantitie();
        productService.addProducts(productId, quantity);
    });
        return or;
    }

    @Override
    public OrderResponse confirmOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(()-> new RuntimeException());
        order.setStatus(OrderStatus.CONFIRMED);
        return orderMapper.toDto(orderRepository.save(order));
    }
}
