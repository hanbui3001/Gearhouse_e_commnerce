package com.example.demo_ecommerce.validation.annotation;

import com.example.demo_ecommerce.validation.validator.ValidEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD})
@Constraint(validatedBy = {ValidEmailValidator.class})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidEmail {
    String message() default "Email should be valid form";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
