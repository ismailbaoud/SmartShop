package com.ismail.smartShop.exception.product;

import org.springframework.http.HttpStatus;

import com.ismail.smartShop.exception.AppException;

public class QuantityNotAvailableException extends AppException{
    public QuantityNotAvailableException() {
        super("this quantity is not available", HttpStatus.BAD_REQUEST);
    }
}
