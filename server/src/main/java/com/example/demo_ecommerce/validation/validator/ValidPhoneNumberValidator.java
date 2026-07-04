package com.example.demo_ecommerce.validation.validator;

import com.example.demo_ecommerce.validation.annotation.ValidPhoneNumber;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidPhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return value.matches("^(09|03)\\d{8}$");
    }
}
