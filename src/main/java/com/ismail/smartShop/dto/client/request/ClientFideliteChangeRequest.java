package com.ismail.smartShop.dto.client.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientFideliteChangeRequest {

    @NotBlank (message = "loyality is required")
    private String niveauDeFidelite;
}
