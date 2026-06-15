package com.example.demo_ecommerce.service;

import com.example.demo_ecommerce.model.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

public interface JwtService {
    String generateAccessToken(String userId, List<String> authorities);
    String generateRefreshToken(String userId);
    SignedJWT verifyRefreshToken(String refreshToken) throws ParseException, JOSEException;
}
