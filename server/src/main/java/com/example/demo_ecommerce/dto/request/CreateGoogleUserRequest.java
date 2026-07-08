package com.example.demo_ecommerce.dto.request;

import com.example.demo_ecommerce.enums.AuthProvider;
import lombok.Builder;

@Builder
public record CreateGoogleUserRequest (
        String email,
        String fullName,
        AuthProvider authProvider,
        String providerId,
        String avatarUrl
)
{
}
