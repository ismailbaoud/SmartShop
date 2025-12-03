package com.ismail.smartShop.dto.user.request;

import com.ismail.smartShop.model.enums.Role;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRequest {

    @NotBlank (message = "user name is required")
    private String userName;

    @NotBlank (message = "password is required")
    private String password;

    @NotBlank (message = "role is required")
    private Role role;

    private Long client_id;
}
