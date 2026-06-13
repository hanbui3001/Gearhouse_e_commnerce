package com.example.demo_ecommerce.service.impl;

import com.example.demo_ecommerce.enums.Token;
import com.example.demo_ecommerce.exception.CustomException;
import com.example.demo_ecommerce.exception.ErrorCode;
import com.example.demo_ecommerce.model.User;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.issuer}")
    private String issuer;
    @Override
    public String generateAccessToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        List<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        //payload
        Date now = new Date();
        Date expiration = Date.from(now.toInstant().plus(30, ChronoUnit.HOURS));
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer(this.issuer)
                .issueTime(now)
                .expirationTime(expiration)
                .claim("authorities", authorities)
                .claim("typ", Token.ACCESS.name())
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
                .issuer(this.issuer)
                .issueTime(now)
                .expirationTime(expiration)
                .claim("typ", Token.REFRESH.name())
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
