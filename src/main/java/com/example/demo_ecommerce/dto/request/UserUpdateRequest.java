package com.example.demo_ecommerce.dto.request;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserUpdateRequest(
        String fullName,
        String phoneNumber,
        LocalDate dateOfBirth
) {
}
