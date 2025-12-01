package com.ismail.smartShop.mapper;

import org.mapstruct.Mapper;

import com.ismail.smartShop.dto.orderItem.Request.OrderItemRequest;
import com.ismail.smartShop.dto.orderItem.response.OrderItemResponse;
import com.ismail.smartShop.model.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItem toEntity(OrderItemRequest oiReq);
    OrderItemResponse toDto(OrderItem oi);
}
