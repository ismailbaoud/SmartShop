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

import com.ismail.smartShop.dto.user.request.UserRequest;
import com.ismail.smartShop.dto.user.response.UserResponse;
import com.ismail.smartShop.helper.passwordHasher;
import com.ismail.smartShop.mapper.UserMapper;
import com.ismail.smartShop.model.User;
import com.ismail.smartShop.model.enums.Role;
import com.ismail.smartShop.repository.UserRepository;
import com.ismail.smartShop.service.implementation.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private passwordHasher passwordHasher;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUserName("admin@test.com");
        user.setPassword("hashedPassword");
        user.setRole(Role.CLIENT);

        userRequest = new UserRequest();
        userRequest.setUserName("admin@test.com");
        userRequest.setPassword("password123");
        userRequest.setRole(Role.CLIENT);

        userResponse = new UserResponse(1L, "admin@test.com", Role.CLIENT, null);
    }

    @Test
    void createUser_ShouldCreateUserWithHashedPassword_WhenValidRequest() {
        // Arrange
        when(passwordHasher.hash("password123")).thenReturn("hashedPassword");
        when(userMapper.toEntity(any(UserRequest.class))).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userResponse);

        // Act
        UserResponse result = userService.createUser(userRequest);

        // Assert
        assertNotNull(result);
        assertEquals("admin@test.com", result.username());
        assertEquals(Role.CLIENT, result.role());
        verify(passwordHasher, times(1)).hash("password123");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getUserById_ShouldThrowUnsupportedOperationException() {
        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> userService.getUserById(1L));
    }

    @Test
    void updateUser_ShouldThrowUnsupportedOperationException() {
        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> userService.updateUser(userRequest));
    }

    @Test
    void deleteUserById_ShouldThrowUnsupportedOperationException() {
        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> userService.deleteUserById(1L));
    }

    @Test
    void getAllUsers_ShouldThrowUnsupportedOperationException() {
        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> userService.getAllUsers());
    }
}

