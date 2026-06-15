package com.example.demo_ecommerce.service.impl;

import com.example.demo_ecommerce.dto.request.AuthenticateRequest;
import com.example.demo_ecommerce.dto.response.AuthenticateResponse;
import com.example.demo_ecommerce.exception.CustomException;
import com.example.demo_ecommerce.exception.ErrorCode;
import com.example.demo_ecommerce.model.User;
import com.example.demo_ecommerce.repository.UserRepository;
import com.example.demo_ecommerce.service.AuthenticationService;
import com.example.demo_ecommerce.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public AuthenticateResponse authenticate(AuthenticateRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        var authentication = authenticationManager.authenticate(authenticationToken);

        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user.getId());
        String refreshToken = jwtService.generateRefreshToken(user.getId());
        return AuthenticateResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
