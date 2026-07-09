package com.example.demo_ecommerce.dto.response;

import lombok.Builder;

@Builder
public record CategoryResponse(
        String name,
        String slug
) {
}
