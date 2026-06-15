package com.example.demo_ecommerce.service;

import com.example.demo_ecommerce.dto.request.AuthenticateRequest;
import com.example.demo_ecommerce.dto.response.AuthenticateResponse;

public interface AuthenticationService {
    AuthenticateResponse authenticate(AuthenticateRequest request);
    AuthenticateResponse refreshToken(String refreshToken);
}
