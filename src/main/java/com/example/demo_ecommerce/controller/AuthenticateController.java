package com.example.demo_ecommerce.controller;

import com.example.demo_ecommerce.dto.request.AuthenticateRequest;
import com.example.demo_ecommerce.dto.response.ApiResponse;
import com.example.demo_ecommerce.dto.response.AuthenticateResponse;
import com.example.demo_ecommerce.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class AuthenticateController {
    private final AuthenticationService authenticationService;
    @PostMapping("/authenticate")
    public ApiResponse<AuthenticateResponse> authenticate(@RequestBody AuthenticateRequest authenticateRequest) {
        var authenticate = authenticationService.authenticate(authenticateRequest);
        return ApiResponse.<AuthenticateResponse>builder()
                .code(200)
                .message("user authenticated")
                .data(authenticate)
                .build();
    }
}
