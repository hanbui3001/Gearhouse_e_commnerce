package com.example.demo_ecommerce.exception;

import lombok.Builder;

import java.time.LocalTime;

@Builder
public record ErrorResponse(
        int status,
        String message,
        String error,
        LocalTime timestamp,
        String path
) {
}
