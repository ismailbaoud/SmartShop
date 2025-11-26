package com.ismail.smartShop.service.implementation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ismail.smartShop.dto.product.request.ProductRequest;
import com.ismail.smartShop.dto.product.response.ProductResponse;
import com.ismail.smartShop.exception.product.ProductNotFoundException;
import com.ismail.smartShop.mapper.ProductMapper;
import com.ismail.smartShop.model.Product;
import com.ismail.smartShop.repository.ProductRepository;
import com.ismail.smartShop.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse createProduct(ProductRequest req) {
        Product p = productMapper.toEntity(req);
        p.setCreatedAt(LocalDateTime.now());
        return productMapper.toDto(productRepository.save(p));
    }

    @Override
    public ProductResponse getProductById(Long id) {
        return productMapper.toDto(productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException()));
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest req) {
        Product p = productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException());
        p.setNom(req.getNom());
        p.setPrixUnit(req.getPrixUnit());
        p.setStockQuantitie(req.getStockQuantitie());
        p.setUpdatedAt(LocalDateTime.now());

        return productMapper.toDto(productRepository.save(p));
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(p-> productMapper.toDto(p)).toList();
    }
    
}
