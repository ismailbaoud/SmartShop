package com.ismail.smartShop.mapper;

import org.mapstruct.Mapper;

import com.ismail.smartShop.dto.product.request.ProductRequest;
import com.ismail.smartShop.dto.product.response.ProductResponse;
import com.ismail.smartShop.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductRequest request);
    ProductResponse toDto(Product p);
}
