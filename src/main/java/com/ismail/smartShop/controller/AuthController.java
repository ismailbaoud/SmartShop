package com.ismail.smartShop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismail.smartShop.dto.auth.request.LoginRequest;
import com.ismail.smartShop.model.User;
import com.ismail.smartShop.service.AuthService;
import com.ismail.smartShop.service.implementation.AuthServiceImpl;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session) {
        User loggedUser = authService.login(request, session);
        return ResponseEntity.ok("Logged in as: " + loggedUser.getUserName());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        authService.logout(session);
        return ResponseEntity.ok("Logged out");
    }
}
