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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Override
    public AuthenticateResponse authenticate(AuthenticateRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHENTICATED));
        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new CustomException(ErrorCode.UNAUTHENTICATED);
        }
        String accessToken = jwtService.generateAccessToken(user.getId());
        String refreshToken = UUID.randomUUID().toString();
        return AuthenticateResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
