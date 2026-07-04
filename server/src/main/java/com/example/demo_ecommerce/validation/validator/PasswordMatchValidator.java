package com.example.demo_ecommerce.validation.validator;

import com.example.demo_ecommerce.dto.request.ResetPasswordRequest;
import com.example.demo_ecommerce.dto.request.UserRegisterRequest;
import com.example.demo_ecommerce.validation.annotation.PasswordMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        if (value instanceof UserRegisterRequest request) {
            return request.password() != null && request.password().equals(request.confirmPassword());
        }
        if (value instanceof ResetPasswordRequest request) {
            return request.newPassword() != null && request.newPassword().equals(request.confirmNewPassword());
        }
        return false;
    }
}
