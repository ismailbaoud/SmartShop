package com.ismail.smartShop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismail.smartShop.annotation.RequireAdmin;
import com.ismail.smartShop.annotation.RequireAuth;
import com.ismail.smartShop.annotation.RequireClient;
import com.ismail.smartShop.dto.product.request.ProductRequest;
import com.ismail.smartShop.dto.product.response.ProductResponse;
import com.ismail.smartShop.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @RequireAdmin
    @RequireAuth
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest req) {
        return ResponseEntity.ok().body(productService.createProduct(req));
    }
    
    @GetMapping
    @RequireAuth
    @RequireAdmin
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok().body(productService.getAllProducts());
    }
    
    @GetMapping("/{id}")
    @RequireAuth
    @RequireAdmin
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(productService.getProductById(id));
    }
    
    @PutMapping("/{id}")
    @RequireAuth
    @RequireAdmin
    public ResponseEntity<ProductResponse> updateProduct(@Valid @PathVariable Long id, @RequestBody ProductRequest req) {
        return ResponseEntity.ok().body(productService.updateProduct(id, req));
    }

    @DeleteMapping("/{id}")
    @RequireAuth
    @RequireAdmin
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().body("product deleted successfully");
    }
}   
