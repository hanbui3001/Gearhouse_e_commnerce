package com.example.demo_ecommerce.dto.response;

import lombok.Builder;

@Builder
public record PresignUrlResponse(
        String url,
        String key
) {
}
