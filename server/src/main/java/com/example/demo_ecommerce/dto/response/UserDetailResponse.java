package com.example.demo_ecommerce.dto.response;

import java.time.LocalDate;

public record UserDetailResponse(
        String id,
        String email,
        String fullName,
        String phoneNumber,
        LocalDate dateOfBirth,
        String status
) {
}
