package com.ismail.smartShop.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ismail.smartShop.dto.user.request.UserRequest;
import com.ismail.smartShop.dto.user.response.UserResponse;
import com.ismail.smartShop.service.UserService;

@Service
public class UserServiceImpl implements UserService{


    @Override
    public UserResponse createUser(UserRequest ur) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createUser'");
    }

    @Override
    public UserResponse getUserById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserById'");
    }

    @Override
    public UserResponse updateUser(UserRequest ur) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public UserResponse deleteUserById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteUserById'");
    }

    @Override
    public List<UserResponse> getAllUsers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllUsers'");
    }


}
