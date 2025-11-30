package com.ismail.smartShop.service;

import com.ismail.smartShop.dto.auth.request.LoginRequest;
import com.ismail.smartShop.model.User;

import jakarta.servlet.http.HttpSession;

public interface AuthService {
    User login(LoginRequest logReq, HttpSession session);
    void logout(HttpSession sessionà);

}
