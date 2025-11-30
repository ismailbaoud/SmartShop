package com.ismail.smartShop.service.implementation;

import java.net.http.HttpRequest;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ismail.smartShop.dto.auth.request.LoginRequest;
import com.ismail.smartShop.model.User;
import com.ismail.smartShop.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public User login(LoginRequest logReq, HttpSession session) {
        User user = userRepository.findByUserName(logReq.getUsername());

    if (!passwordEncoder.matches(logReq.getPassword(), user.getPassword())) {
            throw new RuntimeException("password incorrect");
        }

        session.setAttribute("USER_ID", user.getId());

        return user;
    }


    public void logout(HttpSession session) {
        session.invalidate();
    }
}
