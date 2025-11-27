package com.ismail.smartShop.mapper;

import org.mapstruct.Mapper;

import com.ismail.smartShop.dto.order.request.OrderRequest;
import com.ismail.smartShop.dto.order.response.OrderResponse;
import com.ismail.smartShop.model.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toEntity(OrderRequest orderReq);
    OrderResponse toDto(Order order);
}
