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
    public String generateToken(String userId, long time, ChronoUnit unit, String type) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);


        //payload
        Date now = new Date();
        Date expiration = Date.from(now.toInstant().plus(time, unit));
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userId)
                .issueTime(now)
                .expirationTime(expiration)
                .claim("type", type)
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
    public String generateAccessToken(String userId) {
        return generateToken(userId, 5, ChronoUnit.MINUTES, "access");

    }

    @Override
    public String generateRefreshToken(String userId) {
        return generateToken(userId, 5, ChronoUnit.DAYS, "refresh");

    }
}
