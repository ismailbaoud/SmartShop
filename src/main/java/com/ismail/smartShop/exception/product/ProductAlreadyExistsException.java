package com.ismail.smartShop.exception.product;

import org.springframework.http.HttpStatus;

import com.ismail.smartShop.exception.AppException;

public class ProductAlreadyExistsException extends AppException{
    public ProductAlreadyExistsException() {
        super("this name of product is already exists", HttpStatus.BAD_REQUEST);
    }
}
