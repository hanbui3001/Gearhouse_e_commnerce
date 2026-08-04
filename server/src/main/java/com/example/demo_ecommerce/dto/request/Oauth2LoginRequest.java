package com.example.demo_ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;

public record Oauth2LoginRequest(
        @NotBlank String code
) {
}
