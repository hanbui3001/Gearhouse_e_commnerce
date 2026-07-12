package com.example.demo_ecommerce.service;

import java.util.Map;

public interface EmailService {
    void sendPasswordResetEmail(String email, String token);

    void sendEmailBySendGrid(String toEmail, String templateKey, Map<String, Object> dynamicData);

}
