package com.ismail.smartShop.exception.client;

import org.springframework.http.HttpStatus;

import com.ismail.smartShop.exception.AppException;

public class ClientAlreadyExistException extends AppException {
    public ClientAlreadyExistException() {
        super("the client is already exist", HttpStatus.CONFLICT);
    }
}
