package com.example.demo_ecommerce.service;

import com.example.demo_ecommerce.dto.request.AuthenticateRequest;
import com.example.demo_ecommerce.dto.response.AuthenticateResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticateResponse authenticate(AuthenticateRequest request) throws ParseException;
    AuthenticateResponse refreshToken(String refreshToken);
    void logout(String acessToken, String refreshToken ) throws ParseException, JOSEException;

}
