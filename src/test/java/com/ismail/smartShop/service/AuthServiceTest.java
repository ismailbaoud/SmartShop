package com.ismail.smartShop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ismail.smartShop.dto.auth.request.LoginRequest;
import com.ismail.smartShop.helper.passwordHasher;
import com.ismail.smartShop.model.User;
import com.ismail.smartShop.model.enums.Role;
import com.ismail.smartShop.repository.UserRepository;
import com.ismail.smartShop.service.implementation.AuthServiceImpl;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private passwordHasher passwordHasher;

    @Mock
    private HttpSession session;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUserName("test@test.com");
        user.setPassword("hashedPassword");
        user.setRole(Role.CLIENT);

        loginRequest = new LoginRequest();
        loginRequest.setUsername("test@test.com");
        loginRequest.setPassword("password123");
    }

    @Test
    void login_ShouldReturnUser_WhenCredentialsAreValid() {
        // Arrange
        when(userRepository.findByUserName("test@test.com")).thenReturn(user);
        when(passwordHasher.check("password123", "hashedPassword")).thenReturn(true);
        doNothing().when(session).setAttribute("USER", user);
        doNothing().when(session).setMaxInactiveInterval(3600);

        // Act
        User result = authService.login(loginRequest, session);

        // Assert
        assertNotNull(result);
        assertEquals("test@test.com", result.getUserName());
        assertEquals(Role.CLIENT, result.getRole());
        verify(userRepository, times(1)).findByUserName("test@test.com");
        verify(passwordHasher, times(1)).check("password123", "hashedPassword");
        verify(session, times(1)).setAttribute("USER", user);
        verify(session, times(1)).setMaxInactiveInterval(3600);
    }

    @Test
    void login_ShouldThrowException_WhenPasswordIsInvalid() {
        // Arrange
        when(userRepository.findByUserName("test@test.com")).thenReturn(user);
        when(passwordHasher.check("wrongPassword", "hashedPassword")).thenReturn(false);

        loginRequest.setPassword("wrongPassword");

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> authService.login(loginRequest, session));
        
        assertEquals("Invalid username or password", exception.getMessage());
        verify(userRepository, times(1)).findByUserName("test@test.com");
        verify(passwordHasher, times(1)).check("wrongPassword", "hashedPassword");
        verify(session, never()).setAttribute(anyString(), any());
    }

    @Test
    void logout_ShouldInvalidateSession() {
        // Arrange
        doNothing().when(session).invalidate();

        // Act
        authService.logout(session);

        // Assert
        verify(session, times(1)).invalidate();
    }
}

