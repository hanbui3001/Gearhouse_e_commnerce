package com.example.demo_ecommerce.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record UserRoleResponse(
        String id,
        String email,
        List<String> roles
) {
}
