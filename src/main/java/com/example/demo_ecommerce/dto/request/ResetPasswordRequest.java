package com.example.demo_ecommerce.dto.request;

import com.example.demo_ecommerce.validation.annotation.PasswordMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@PasswordMatch
public record ResetPasswordRequest(
        String token,
        @NotBlank(message = "password is required")
        @Size(min = 6, message = "password is greater than 6 characters")
        String newPassword,
        String confirmNewPassword
) {

}
