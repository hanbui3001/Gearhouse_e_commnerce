package com.example.demo_ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank(message = "name is required")
        String name,
        @NotBlank(message = "message is required")
        String slug
) {
}
