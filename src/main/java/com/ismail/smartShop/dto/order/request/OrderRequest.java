package com.ismail.smartShop.dto.order.request;

import java.util.List;


import com.ismail.smartShop.dto.orderItem.Request.OrderItemRequest;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Long client_id;
    private String promo;
    @Nullable
    private Double tva;
    @NotEmpty(message = "The order must contain at least 1 item")
    @Size(min = 1, message = "The order must contain at least 1 item")
    private List<OrderItemRequest> items;
}
