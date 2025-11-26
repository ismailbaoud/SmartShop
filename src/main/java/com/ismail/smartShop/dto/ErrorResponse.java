package com.ismail.smartShop.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

@Builder
public record ErrorResponse(
    int status,
    String message,
    String path,
    LocalDateTime timestamp,
    List<String> details
) {}
