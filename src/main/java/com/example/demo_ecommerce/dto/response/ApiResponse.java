package com.example.demo_ecommerce.dto.response;

import lombok.Builder;

@Builder
public record ApiResponse<T>(
        int code,
        String message,
        T data
) {
}
