package com.example.demo_ecommerce.service;

import com.example.demo_ecommerce.dto.request.UserRegisterRequest;
import com.example.demo_ecommerce.dto.response.PageResponse;
import com.example.demo_ecommerce.dto.response.UserDetailResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

public interface UserService {
    UserDetailResponse registerUser(UserRegisterRequest request);
    PageResponse<UserDetailResponse> getUsers(int page, int size, String email, String fullName, String phoneNumber);
    UserDetailResponse getMyProfile(@AuthenticationPrincipal Jwt jwt);
    UserDetailResponse getUserById(String id);
}
