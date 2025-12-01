package com.ismail.smartShop.service.implementation;


import org.springframework.stereotype.Service;

import com.ismail.smartShop.dto.auth.request.LoginRequest;
import com.ismail.smartShop.helper.passwordHasher;
import com.ismail.smartShop.model.User;
import com.ismail.smartShop.repository.UserRepository;
import com.ismail.smartShop.service.AuthService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final passwordHasher passwordHasher;

    @Override
    public User login(LoginRequest request, HttpSession session) {
        User user = userRepository.findByUserName(request.getUsername());
        if (!passwordHasher.check(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        session.setAttribute("USER", user);
        session.setMaxInactiveInterval(60 * 60);

        return user;
    }

    @Override
    public void logout(HttpSession session) {
        session.invalidate();
    }
}
