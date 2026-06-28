package com.example.demo_ecommerce.dto.request;

import jakarta.validation.Valid;
import lombok.Builder;

@Builder
public record EmailRequest(
        @Valid
        String email
) {
}
