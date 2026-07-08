package com.example.demo_ecommerce.dto.internal;

import lombok.Builder;

@Builder
public record GoogleInfoDetails(
        String providerId,
        String email,
        boolean verified,
        String name,
        String picture
) {
}
