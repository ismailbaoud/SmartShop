package com.ismail.smartShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ismail.smartShop.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String name);
}
