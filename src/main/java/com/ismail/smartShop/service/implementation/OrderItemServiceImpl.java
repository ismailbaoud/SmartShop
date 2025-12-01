package com.ismail.smartShop.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ismail.smartShop.dto.orderItem.Request.OrderItemRequest;
import com.ismail.smartShop.dto.orderItem.response.OrderItemResponse;
import com.ismail.smartShop.exception.product.ProductNotFoundException;
import com.ismail.smartShop.mapper.OrderItemMapper;
import com.ismail.smartShop.model.Order;
import com.ismail.smartShop.model.OrderItem;
import com.ismail.smartShop.model.Product;
import com.ismail.smartShop.repository.OrderItemRepository;
import com.ismail.smartShop.repository.ProductRepository;
import com.ismail.smartShop.service.OrderItemService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final ProductRepository productRepository;
    
    @Override
    public OrderItemResponse createOrderItem(OrderItemRequest req, Order order) {
        Product p = productRepository.findById(req.getProduct_id()).orElseThrow(() -> new ProductNotFoundException());
        Double totalLine = p.getPrixUnit() * req.getQuantite();
        OrderItem oi = orderItemMapper.toEntity(req);
        oi.setProduct(p);
        oi.setPrixUnitaire(p.getPrixUnit());
        oi.setLineTotal(totalLine);
        oi.setOrder(order);

        return  orderItemMapper.toDto(orderItemRepository.save(oi));
    }

    @Override
    public List<OrderItemResponse> getAllOrderItem() {
        return orderItemRepository.findAll().stream().map(a -> orderItemMapper.toDto(a)).toList();
    }
    
}
