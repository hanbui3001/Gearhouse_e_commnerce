package com.example.demo_ecommerce.service.impl;

import com.example.demo_ecommerce.dto.request.AuthenticateRequest;
import com.example.demo_ecommerce.dto.response.AuthenticateResponse;
import com.example.demo_ecommerce.exception.CustomException;
import com.example.demo_ecommerce.exception.ErrorCode;
import com.example.demo_ecommerce.model.User;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    @Override
    public AuthenticateResponse authenticate(AuthenticateRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        var authentication = authenticationManager.authenticate(authenticationToken);

        User user = (User) authentication.getPrincipal();
        List<String> authorities = SecurityUtil.getAuthorities(user);
        String accessToken = jwtService.generateAccessToken(user.getId(), authorities);
        String refreshToken = jwtService.generateRefreshToken(user.getId());

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

            List<String> authorities = SecurityUtil.getAuthorities(user);
            String accessToken = jwtService.generateAccessToken(user.getId(), authorities);
            return AuthenticateResponse.builder()
                    .accessToken(accessToken)
                    .build();
        } catch (ParseException | JOSEException e) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }

}
