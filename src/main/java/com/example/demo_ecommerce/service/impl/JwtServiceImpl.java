package com.example.demo_ecommerce.service.impl;

import com.example.demo_ecommerce.exception.CustomException;
import com.example.demo_ecommerce.exception.ErrorCode;
import com.example.demo_ecommerce.service.JwtService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Date;
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secret-key}")
    private String secretKey;


    @Override
    public String generateAccessToken(String userId) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);


        //payload
        Date now = new Date();
        Date expiration = Date.from(now.toInstant().plus(30, ChronoUnit.HOURS));
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userId)
                .issueTime(now)
                .expirationTime(expiration)
                .audience("access")
                .build();

        SignedJWT jwt = new SignedJWT(jwsHeader, claimsSet);
        try {
            jwt.sign(new MACSigner(secretKey));
            return jwt.serialize();
        } catch (JOSEException e) {
            throw new CustomException(ErrorCode.GENERATE_JWT_ERROR);
        }

    }

    @Override
    public String generateRefreshToken(String userId) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);


        //payload
        Date now = new Date();
        Date expiration = Date.from(now.toInstant().plus(10, ChronoUnit.DAYS));
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userId)
                .issueTime(now)
                .expirationTime(expiration)
                .audience("refresh")
                .build();

        SignedJWT jwt = new SignedJWT(jwsHeader, claimsSet);
        try {
            jwt.sign(new MACSigner(secretKey));
            return jwt.serialize();
        } catch (JOSEException e) {
            throw new CustomException(ErrorCode.GENERATE_JWT_ERROR);
        }

    }
}
