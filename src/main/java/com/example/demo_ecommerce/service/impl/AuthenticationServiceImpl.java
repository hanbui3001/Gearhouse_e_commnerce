package com.example.demo_ecommerce.service.impl;

import com.example.demo_ecommerce.constant.EmailSubjectConstant;
import com.example.demo_ecommerce.dto.internal.JwtDetails;
import com.example.demo_ecommerce.dto.request.AuthenticateRequest;
import com.example.demo_ecommerce.dto.request.ResetPasswordRequest;
import com.example.demo_ecommerce.dto.response.AuthenticateResponse;
import com.example.demo_ecommerce.enums.EmailTemplates;
import com.example.demo_ecommerce.enums.TokenType;
import com.example.demo_ecommerce.exception.CustomException;
import com.example.demo_ecommerce.exception.ErrorCode;
import com.example.demo_ecommerce.model.Token;
import com.example.demo_ecommerce.model.User;
import com.example.demo_ecommerce.repository.TokenRepository;
import com.example.demo_ecommerce.repository.UserRepository;
import com.example.demo_ecommerce.service.AuthenticationService;
import com.example.demo_ecommerce.service.EmailService;
import com.example.demo_ecommerce.service.JwtService;
import com.example.demo_ecommerce.utils.SecurityUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Value("${client.frontend-url:http://localhost:3000}")
    private String frontendUrl;
    @Override
    public AuthenticateResponse authenticate(AuthenticateRequest request) throws ParseException {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        var authentication = authenticationManager.authenticate(authenticationToken);

        User user = (User) authentication.getPrincipal();
        List<String> authorities = SecurityUtil.getAuthorities(user);
        JwtDetails accessToken = jwtService.generateAccessToken(user.getId(), authorities);
        JwtDetails refreshToken = jwtService.generateRefreshToken(user.getId());

        tokenRepository.save(Token.builder()
                        .jwtId(refreshToken.getJwtId())
                        .tokenType(TokenType.REFRESH)
                        .timeToLive(refreshToken.getSecondsTtl())
                        .revoked(false)
                        .userId(user.getId())
                .build());

        return AuthenticateResponse.builder()
                .accessToken(accessToken.getValue())
                .refreshToken(refreshToken.getValue())
                .build();
    }

    @Override
    public AuthenticateResponse refreshToken(String refreshToken) {
        if(!StringUtils.hasText(refreshToken)) {
            throw new CustomException(ErrorCode.COOKIE_REQUIRED);
        }
        try {
            SignedJWT signedJWT = jwtService.verifyToken(refreshToken, TokenType.REFRESH);
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
            JwtDetails accessToken = jwtService.generateAccessToken(user.getId(), authorities);
            return AuthenticateResponse.builder()
                    .accessToken(accessToken.getValue())
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
                        .revoked(true)
                        .timeToLive(secondsUntil(signedAccessToken.getJWTClaimsSet().getExpirationTime()))
                        .userId(signedAccessToken.getJWTClaimsSet().getSubject())
                .build());

        if(StringUtils.hasText(refreshToken)){
            SignedJWT signedRefreshToken = jwtService.verifyToken(refreshToken, TokenType.REFRESH);
            String jwtId = signedRefreshToken.getJWTClaimsSet().getJWTID();
            Token refresh = tokenRepository.findById(jwtId)
                    .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));
            refresh.setRevoked(true);
            tokenRepository.save(refresh);
        }
    }

    @Override
    public void forgetPassword(String email) {
        var userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()) {
            return;
        }
        User user = userOptional.get();
        JwtDetails passwordToken = jwtService.generatePasswordToken(user.getId());
        String resetUrl = frontendUrl + "/reset-password?token=" + passwordToken.getValue();
        redisTemplate.opsForValue().set(
                "reset-password:" + passwordToken.getJwtId(),
                user.getId(),
                Duration.ofSeconds(passwordToken.getSecondsTtl())
        );
        long expireMinutes = TimeUnit.SECONDS.toMinutes(passwordToken.getSecondsTtl());
        emailService.sendEmailBySendGrid(user.getEmail(), EmailTemplates.RESET_PASSWORD.getKey(), Map.of(
                "name", user.getFullName(),
                "resetLink", resetUrl,
                "expireMinutes", expireMinutes,
                "subject", EmailSubjectConstant.RESET_PASSWORD_SUBJECT
        ));

    }

    @Override
    public void resetPassword(ResetPasswordRequest request)  {
        try {
            SignedJWT signedPassword = jwtService.verifyToken(request.token(), TokenType.RESET_PASSWORD);
            String resetPasswordKey = "reset-password:" + signedPassword.getJWTClaimsSet().getJWTID();
            String userId = signedPassword.getJWTClaimsSet().getSubject();
            String storedUserId = redisTemplate.opsForValue().get(resetPasswordKey);

            if (!userId.equals(storedUserId)) {
                throw new CustomException(ErrorCode.UNAUTHORIZED);
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
            user.setPassword(passwordEncoder.encode(request.newPassword()));
            userRepository.save(user);
            redisTemplate.delete(resetPasswordKey);
        }catch (ParseException | JOSEException e) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }

    private long secondsUntil(Date expirationTime) {
        return Math.max((expirationTime.getTime() - System.currentTimeMillis()) / 1000, 1);
    }

}
