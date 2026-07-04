package com.example.demo_ecommerce.validation.validator;

import ch.qos.logback.core.util.StringUtil;
import com.example.demo_ecommerce.validation.annotation.ValidEmail;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidEmailValidator implements ConstraintValidator<ValidEmail, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value) || StringUtils.isEmpty(value)) {
            return true;
        }
        return value.endsWith("@gmail.com");
    }
}
