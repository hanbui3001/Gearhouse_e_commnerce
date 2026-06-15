package com.example.demo_ecommerce.controller;

import com.example.demo_ecommerce.dto.request.UserRegisterRequest;
import com.example.demo_ecommerce.dto.request.UserUpdateRequest;
import com.example.demo_ecommerce.dto.response.ApiResponse;
import com.example.demo_ecommerce.dto.response.PageResponse;
import com.example.demo_ecommerce.dto.response.UserDetailResponse;
import com.example.demo_ecommerce.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {
    private final UserService userService;
    @PostMapping("/register")
    public ApiResponse<UserDetailResponse> register(@RequestBody @Valid UserRegisterRequest request) {
        var data = userService.registerUser(request);
        return ApiResponse.<UserDetailResponse>builder()
                .code(201)
                .message("User registered successfully!")
                .data(data)
                .build();
    }
    @GetMapping("/list")
    public ApiResponse<PageResponse<UserDetailResponse>> getAllUsers(@RequestParam(required = false,defaultValue = "1") int page,
                                                                     @RequestParam(required = false,defaultValue = "10") int size,
                                                                     @RequestParam(required = false)String email,
                                                                     @RequestParam(required = false) String fullName,
                                                                     @RequestParam(required = false,defaultValue = "") String phoneNumber) {
        var data = userService.getUsers(page, size, email, fullName, phoneNumber);
        return ApiResponse.<PageResponse<UserDetailResponse>>builder()
                .code(200)
                .message("get users successfully")
                .data(data)
                .build();
    }
    @GetMapping("/profile")
    public ApiResponse<UserDetailResponse> getProfile(@AuthenticationPrincipal Jwt jwt) {
        var data = userService.getMyProfile(jwt);
        return ApiResponse.<UserDetailResponse>builder()
                .code(200)
                .message("get my profile successfully")
                .data(data)
                .build();
    }

    @GetMapping("{id}")
    public ApiResponse<UserDetailResponse> getUser(@PathVariable String id) {
        var data = userService.getUserById(id);
        return ApiResponse.<UserDetailResponse>builder()
                .code(200)
                .message("get user successfully")
                .data(data)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<UserDetailResponse> updateUser(@PathVariable String id, @RequestBody @Valid UserUpdateRequest request) {
        var data = userService.updateUserById(id, request);
        return ApiResponse.<UserDetailResponse>builder()
                .code(200)
                .message("update user successfully")
                .data(data)
                .build();
    }
}
