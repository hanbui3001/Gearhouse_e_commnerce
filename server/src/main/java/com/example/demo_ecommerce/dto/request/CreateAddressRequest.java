package com.example.demo_ecommerce.dto.request;

import com.example.demo_ecommerce.validation.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;

public record CreateAddressRequest(
        @NotBlank
        String receiverName,

        @NotBlank
        @ValidPhoneNumber(message = "phone number should equal 10 numbers and started with 09 and 03")
        String phoneNumber,

        @NotBlank
        String addressLine,

        @NotBlank
        String ward,

        @NotBlank
        String district,

        @NotBlank
        String province

) {
}
