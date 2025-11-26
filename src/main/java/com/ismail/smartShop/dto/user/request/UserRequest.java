package com.ismail.smartShop.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRequest {

    @NotBlank (message = "user name is required")
    private String username;

    @NotBlank (message = "password is required")
    private String password;

    @NotBlank (message = "role is required")
    private String role;

    private Long client_id;
}
