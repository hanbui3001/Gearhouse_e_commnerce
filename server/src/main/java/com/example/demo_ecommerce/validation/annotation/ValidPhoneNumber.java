package com.example.demo_ecommerce.validation.annotation;

import com.example.demo_ecommerce.validation.validator.ValidEmailValidator;
import com.example.demo_ecommerce.validation.validator.ValidPhoneNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD})
@Constraint(validatedBy = {ValidPhoneNumberValidator.class})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidPhoneNumber {
    String message() default "Phone number should be valid form";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
