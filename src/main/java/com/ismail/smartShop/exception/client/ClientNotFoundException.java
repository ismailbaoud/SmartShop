package com.ismail.smartShop.exception.client;

import org.springframework.http.HttpStatus;

import com.ismail.smartShop.exception.AppException;

public class ClientNotFoundException extends AppException {

    public ClientNotFoundException() {
        super("client not found", HttpStatus.NOT_FOUND);
    }
    
}
