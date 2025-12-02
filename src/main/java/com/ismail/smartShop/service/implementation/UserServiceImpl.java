package com.ismail.smartShop.service.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ismail.smartShop.dto.user.request.UserRequest;
import com.ismail.smartShop.dto.user.response.UserResponse;
import com.ismail.smartShop.helper.passwordHasher;
import com.ismail.smartShop.mapper.UserMapper;
import com.ismail.smartShop.repository.UserRepository;
import com.ismail.smartShop.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final passwordHasher passwordHasher;
    @Override
    public UserResponse createUser(UserRequest ur) {
        String passHashed = passwordHasher.hash(ur.getPassword());
        ur.setPassword(passHashed);
        return userMapper.toDto(userRepository.save(userMapper.toEntity(ur)));
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
