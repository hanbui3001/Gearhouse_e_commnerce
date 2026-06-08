package com.example.demo_ecommerce.dto.request;

import com.example.demo_ecommerce.validation.annotation.ValidEmail;
import com.example.demo_ecommerce.validation.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder

public record UserRegisterRequest(
        @NotBlank(message = "email is required")
        @Email
        String email,
        @NotBlank(message = "password is required")
        @Size(min = 6, message = "password is greater than 6 characters")
        String password,
        @NotBlank(message = "full name is required")
        String fullName,
        @NotBlank(message = "phone number is required")
        @ValidPhoneNumber(message = "phone number should equal 10 numbers and started with 09 and 03")
        String phoneNumber,
        @NotNull(message = "email is required")
        LocalDate dateOfBirth
){
}
