package com.ismail.smartShop.service;


import java.util.List;

import com.ismail.smartShop.dto.user.request.UserRequest;
import com.ismail.smartShop.dto.user.response.UserResponse;

public interface UserService {
 
    public UserResponse createUser(UserRequest ur);

    public UserResponse getUserById(Long id);

    public UserResponse updateUser(UserRequest ur);

    public UserResponse deleteUserById(Long id);

    public List<UserResponse> getAllUsers();
    
}
