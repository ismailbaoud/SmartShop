package com.ismail.smartShop.dto.user.response;

import com.ismail.smartShop.model.Client;
import com.ismail.smartShop.model.enums.Role;

public record UserResponse(
    Long id,
    String userName,
    Role role,
    Client client
){}