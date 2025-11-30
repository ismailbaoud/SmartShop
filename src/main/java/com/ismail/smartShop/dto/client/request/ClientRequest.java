package com.ismail.smartShop.dto.client.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientRequest {
    @NotBlank (message = "name is required")
    private String nom;

    @NotBlank (message = "email is required")
    @Email (message = "you should enter correct email")
    private String email;

    private String password;
}