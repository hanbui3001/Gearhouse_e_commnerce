package com.example.demo_ecommerce.service.impl;

import com.example.demo_ecommerce.dto.request.AuthenticateRequest;
import com.example.demo_ecommerce.dto.response.AuthenticateResponse;
import com.example.demo_ecommerce.enums.TokenType;
import com.example.demo_ecommerce.exception.CustomException;
import com.example.demo_ecommerce.exception.ErrorCode;
import com.example.demo_ecommerce.model.Token;
import com.example.demo_ecommerce.model.User;
import com.example.demo_ecommerce.repository.TokenRepository;
import com.example.demo_ecommerce.repository.UserRepository;
import com.example.demo_ecommerce.service.AuthenticationService;
import com.example.demo_ecommerce.service.JwtService;
import com.example.demo_ecommerce.utils.SecurityUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    @Override
    public AuthenticateResponse authenticate(AuthenticateRequest request) throws ParseException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        var authentication = authenticationManager.authenticate(authenticationToken);

        User user = (User) authentication.getPrincipal();
        List<String> authorities = SecurityUtil.getAuthorities(user);
        String accessToken = jwtService.generateAccessToken(user.getId(), authorities);
        String refreshToken = jwtService.generateRefreshToken(user.getId());

        SignedJWT signedRefreshToken = SignedJWT.parse(refreshToken);
        TokenType tokenType = TokenType.valueOf(signedRefreshToken.getJWTClaimsSet().getStringClaim("typ"));
        tokenRepository.save(Token.builder()
                        .jwtId(signedRefreshToken.getJWTClaimsSet().getJWTID())
                        .expiredTime(signedRefreshToken.getJWTClaimsSet().getExpirationTime())
                        .tokenType(tokenType)
                        .revoked(false)
                        .userId(signedRefreshToken.getJWTClaimsSet().getSubject())
                .build());

        return AuthenticateResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticateResponse refreshToken(String refreshToken) {
        if(!StringUtils.hasText(refreshToken)) {
            throw new CustomException(ErrorCode.COOKIE_REQUIRED);
        }
        try {
            SignedJWT signedJWT = jwtService.verifyRefreshToken(refreshToken);
            String userId = signedJWT.getJWTClaimsSet().getSubject();
            User user = userRepository.findByIdWithRoles(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
            Token refresh = tokenRepository.findById(jwtId)
                    .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));
            if(refresh.isRevoked()){
                throw new CustomException(ErrorCode.UNAUTHORIZED);
            }
            List<String> authorities = SecurityUtil.getAuthorities(user);
            String accessToken = jwtService.generateAccessToken(user.getId(), authorities);
            return AuthenticateResponse.builder()
                    .accessToken(accessToken)
                    .build();
        } catch (ParseException | JOSEException e) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }

    @Override
    public void logout(String authorizationHeader, String refreshToken) throws ParseException, JOSEException {
        if(!StringUtils.hasText(refreshToken) || !authorizationHeader.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        String accessToken = authorizationHeader.substring(7);
        SignedJWT signedAccessToken = SignedJWT.parse(accessToken);
        TokenType tokenType = TokenType.valueOf(signedAccessToken.getJWTClaimsSet().getStringClaim("typ"));
        tokenRepository.save(Token.builder()
                        .jwtId(signedAccessToken.getJWTClaimsSet().getJWTID())
                        .tokenType(tokenType)
                        .expiredTime(signedAccessToken.getJWTClaimsSet().getExpirationTime())
                        .revoked(true)
                        .userId(signedAccessToken.getJWTClaimsSet().getSubject())
                .build());

        if(StringUtils.hasText(refreshToken)){
            SignedJWT signedRefreshToken = jwtService.verifyRefreshToken(refreshToken);
            String jwtId = signedRefreshToken.getJWTClaimsSet().getJWTID();
            Token refresh = tokenRepository.findById(jwtId)
                    .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));
            refresh.setRevoked(true);
            tokenRepository.save(refresh);
        }
    }

}
