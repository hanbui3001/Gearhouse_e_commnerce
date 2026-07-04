package com.example.demo_ecommerce.enums;

import lombok.Getter;

@Getter
public enum EmailTemplates {
    WELCOME("welcome"),
    RESET_PASSWORD("reset-password"),

    ;

    private final String key;
    EmailTemplates(String key) {
        this.key = key;
    }
}
