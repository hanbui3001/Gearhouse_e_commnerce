package com.example.demo_ecommerce.dto.response;

import lombok.Builder;

@Builder
public record AuthenticateResponse(
        String accessToken,
        String refreshToken
) {
}
