package com.example.demo_ecommerce.controller;

import com.example.demo_ecommerce.dto.request.UserRegisterRequest;
import com.example.demo_ecommerce.dto.response.ApiResponse;
import com.example.demo_ecommerce.dto.response.UserDetailResponse;
import com.example.demo_ecommerce.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;
    @PostMapping("/register")
    public ApiResponse<UserDetailResponse> register(@RequestBody @Valid UserRegisterRequest request) throws Exception {
        var data = userService.registerUser(request);
        return ApiResponse.<UserDetailResponse>builder()
                .code(201)
                .message("User registered successfully!")
                .data(data)
                .build();
    }
}
