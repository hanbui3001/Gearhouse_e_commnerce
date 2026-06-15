package com.example.demo_ecommerce.service;

import java.time.temporal.ChronoUnit;

public interface JwtService {
    String generateAccessToken(String userId);
    String generateRefreshToken(String userId);
}
