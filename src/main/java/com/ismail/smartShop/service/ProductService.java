package com.ismail.smartShop.service;

import java.util.List;

import com.ismail.smartShop.dto.product.request.ProductRequest;
import com.ismail.smartShop.dto.product.response.ProductResponse;

public interface ProductService {
    
    ProductResponse createProduct(ProductRequest request);

    ProductResponse getProductById(Long id);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);
    
    List<ProductResponse> getAllProducts();

    Integer discountProducts(Long id , Integer pQty);
    
    Integer addProducts(Long id , Integer pQty);
}
