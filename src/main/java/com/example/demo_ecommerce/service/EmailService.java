package com.example.demo_ecommerce.service;

public interface EmailService {
    void sendPasswordResetEmail(String email, String token);

}
