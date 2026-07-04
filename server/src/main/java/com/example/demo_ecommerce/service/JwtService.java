package com.example.demo_ecommerce.service;

import com.example.demo_ecommerce.dto.internal.JwtDetails;
import com.example.demo_ecommerce.enums.TokenType;
import com.example.demo_ecommerce.model.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

public interface JwtService {
    JwtDetails generateAccessToken(String userId, List<String> authorities);
    JwtDetails generateRefreshToken(String userId);
    SignedJWT verifyToken(String token, TokenType type) throws ParseException, JOSEException;
    JwtDetails generatePasswordToken(String userId);
}
