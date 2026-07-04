package com.example.demo_ecommerce.controller;

import com.example.demo_ecommerce.dto.request.AuthenticateRequest;
import com.example.demo_ecommerce.dto.request.EmailRequest;
import com.example.demo_ecommerce.dto.request.ResetPasswordRequest;
import com.example.demo_ecommerce.dto.response.ApiResponse;
import com.example.demo_ecommerce.dto.response.AuthenticateResponse;
import com.example.demo_ecommerce.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.Duration;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthenticateController {
    private final AuthenticationService authenticationService;
    @PostMapping("/login")
    public ApiResponse<AuthenticateResponse> authenticate(@RequestBody AuthenticateRequest authenticateRequest,
                                                          HttpServletResponse response) throws ParseException {
        var authenticate = authenticationService.authenticate(authenticateRequest);
        var refreshToken = authenticate.getRefreshToken();
        ResponseCookie cookie =ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(false)
                .domain("localhost")
                .path("/v1/auth")
                .maxAge(Duration.ofDays(7))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        authenticate.setRefreshToken(null);
        return ApiResponse.<AuthenticateResponse>builder()
                .code(200)
                .message("user authenticated")
                .data(authenticate)
                .build();
    }
    @PostMapping("/refresh-token")
    public ApiResponse<AuthenticateResponse> refreshToken(@CookieValue("refresh_token") String refreshToken) {
        var data = authenticationService.refreshToken(refreshToken);
        return ApiResponse.<AuthenticateResponse>builder()
                .code(200)
                .message("refresh token successfully")
                .data(data)
                .build();
    }
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestHeader("Authorization") String authorizationHeader,
                                    @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                    HttpServletResponse response) throws ParseException, JOSEException {
        authenticationService.logout(authorizationHeader, refreshToken);
        ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(false)
                .domain("localhost")
                .path("/v1/auth")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ApiResponse.<Void>builder()
                .code(200)
                .message("logout successfully")
                .build();
    }
    @PostMapping("/forget-password")
    public ApiResponse<Void> forgetPassword(@RequestBody @Valid EmailRequest request){
        authenticationService.forgetPassword(request.email());
        return ApiResponse.<Void>builder()
                .code(200)
                .message("a reset password link has been sent")
                .build();
    }

    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest request) throws ParseException, JOSEException {
        authenticationService.resetPassword(request);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("password reset successfully")
                .build();
    }
}
