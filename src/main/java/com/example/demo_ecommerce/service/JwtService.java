package com.example.demo_ecommerce.service;

import com.example.demo_ecommerce.model.User;

import java.time.temporal.ChronoUnit;

public interface JwtService {
    String generateAccessToken(User user);
    String generateRefreshToken(String userId);
}
