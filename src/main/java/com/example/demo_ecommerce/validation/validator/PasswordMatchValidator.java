package com.example.demo_ecommerce.validation.validator;

import com.example.demo_ecommerce.dto.request.UserRegisterRequest;
import com.example.demo_ecommerce.validation.annotation.PasswordMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, UserRegisterRequest> {
    @Override
    public boolean isValid(UserRegisterRequest value, ConstraintValidatorContext context) {
        return value.password() != null && value.password().equals(value.confirmPassword());
    }
}
