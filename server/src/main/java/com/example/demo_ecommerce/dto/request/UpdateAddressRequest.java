package com.example.demo_ecommerce.dto.request;

import com.example.demo_ecommerce.validation.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;

public record UpdateAddressRequest(
        @NotBlank String receiverName,
        @NotBlank @ValidPhoneNumber String phoneNumber,
        @NotBlank String addressLine,
        @NotBlank String ward,
        @NotBlank String district,
        @NotBlank String province
) {
}
