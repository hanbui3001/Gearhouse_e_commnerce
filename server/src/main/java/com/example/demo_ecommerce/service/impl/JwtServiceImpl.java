package com.example.demo_ecommerce.service.impl;

import com.example.demo_ecommerce.dto.internal.JwtDetails;
import com.example.demo_ecommerce.enums.TokenType;
import com.example.demo_ecommerce.exception.CustomException;
import com.example.demo_ecommerce.exception.ErrorCode;
import com.example.demo_ecommerce.model.User;
import com.example.demo_ecommerce.service.JwtService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.issuer}")
    private String issuer;
    @Override
    public JwtDetails generateAccessToken(String userId, List<String> authorities) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        //payload
        Date now = new Date();
        Date expiration = Date.from(now.toInstant().plus(1, ChronoUnit.MINUTES));
        String jwtId = UUID.randomUUID().toString();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userId)
                .jwtID(jwtId)
                .issuer(this.issuer)
                .issueTime(now)
                .expirationTime(expiration)
                .claim("authorities", authorities)
                .claim("typ", TokenType.ACCESS.name())
                .build();

        SignedJWT jwt = new SignedJWT(jwsHeader, claimsSet);
        try {
            jwt.sign(new MACSigner(secretKey));
            return JwtDetails.builder()
                    .value(jwt.serialize())
                    .jwtId(jwtId)
                    .expiryTime(expiration.getTime())
                    .secondsTtl((expiration.getTime() - new Date().getTime()) / 1000)
                    .build();
        } catch (JOSEException e) {
            throw new CustomException(ErrorCode.GENERATE_JWT_ERROR);
        }

    }

    @Override
    public JwtDetails generateRefreshToken(String userId) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);


        //payload
        Date now = new Date();
        Date expiration = Date.from(now.toInstant().plus(10, ChronoUnit.DAYS));
        String jwtId = UUID.randomUUID().toString();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userId)
                .jwtID(jwtId)
                .issuer(this.issuer)
                .issueTime(now)
                .expirationTime(expiration)
                .claim("typ", TokenType.REFRESH.name())
                .build();

        SignedJWT jwt = new SignedJWT(jwsHeader, claimsSet);
        try {
            jwt.sign(new MACSigner(secretKey));
            return JwtDetails.builder()
                    .value(jwt.serialize())
                    .jwtId(jwtId)
                    .expiryTime(expiration.getTime())
                    .secondsTtl((expiration.getTime() - new Date().getTime()) / 1000)
                    .build();
        } catch (JOSEException e) {
            throw new CustomException(ErrorCode.GENERATE_JWT_ERROR);
        }

    }

    @Override
    public SignedJWT verifyToken(String token, TokenType type) throws ParseException, JOSEException {
        SignedJWT signedJWT =  SignedJWT.parse(token);
        boolean verify = signedJWT.verify(new MACVerifier(secretKey));
        if(!verify){
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        TokenType tokenType = TokenType.valueOf(signedJWT.getJWTClaimsSet().getClaimAsString("typ"));
        if(type != tokenType){
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if(expirationTime.before(new Date())){
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        }
        return signedJWT;

    }

    @Override
    public JwtDetails generatePasswordToken(String userId) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        Date now = new Date();
        Date expiration = Date.from(now.toInstant().plus(10, ChronoUnit.MINUTES));
        String jwtId = UUID.randomUUID().toString();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userId)
                .jwtID(jwtId)
                .issuer(this.issuer)
                .issueTime(now)
                .expirationTime(expiration)
                .claim("typ", TokenType.RESET_PASSWORD.name())
                .build();

        SignedJWT jwt = new SignedJWT(jwsHeader, claimsSet);
        try {
            jwt.sign(new MACSigner(secretKey));
            return JwtDetails.builder()
                    .value(jwt.serialize())
                    .jwtId(jwtId)
                    .expiryTime(expiration.getTime())
                    .secondsTtl((expiration.getTime() - new Date().getTime()) / 1000)
                    .build();
        } catch (JOSEException e) {
            throw new CustomException(ErrorCode.GENERATE_JWT_ERROR);
        }
    }
}
