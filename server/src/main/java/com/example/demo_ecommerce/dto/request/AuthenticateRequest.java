package com.example.demo_ecommerce.dto.request;

import lombok.Builder;

@Builder
public record AuthenticateRequest(
        String email,
        String password
) {
}
