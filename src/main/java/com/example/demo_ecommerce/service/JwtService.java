package com.example.demo_ecommerce.service;

import java.time.temporal.ChronoUnit;

public interface JwtService {
    String generateToken(String userId, long time, ChronoUnit unit, String type);
    String generateAccessToken(String userId);
    String generateRefreshToken(String userId);
}
