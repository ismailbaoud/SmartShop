package com.ismail.smartShop.exception.product;

import org.springframework.http.HttpStatus;

import com.ismail.smartShop.exception.AppException;

public class ProductNotFoundException extends AppException{
    public ProductNotFoundException() {
        super("the product isn't exist", HttpStatus.NOT_FOUND);
    }
}
