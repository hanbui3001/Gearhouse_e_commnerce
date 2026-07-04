package com.example.demo_ecommerce.service;

import com.example.demo_ecommerce.dto.request.ChangeStatusRequest;
import com.example.demo_ecommerce.dto.request.UserRegisterRequest;
import com.example.demo_ecommerce.dto.request.UserRoleRequest;
import com.example.demo_ecommerce.dto.request.UserUpdateRequest;
import com.example.demo_ecommerce.dto.response.PageResponse;
import com.example.demo_ecommerce.dto.response.UserDetailResponse;
import com.example.demo_ecommerce.dto.response.UserRoleResponse;
import com.example.demo_ecommerce.enums.Status;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface UserService {
    UserDetailResponse registerUser(UserRegisterRequest request);
    PageResponse<UserDetailResponse> getUsers(int page, int size, String email, String fullName, String phoneNumber);
    UserDetailResponse getMyProfile(@AuthenticationPrincipal Jwt jwt);
    UserDetailResponse getUserById(String id);
    UserDetailResponse updateUserById(String id,  UserUpdateRequest request);
    UserDetailResponse changeUserStatus(String id, ChangeStatusRequest status);
    //


    UserRoleResponse assignRoles(String id, UserRoleRequest userRoleRequest);
    UserRoleResponse deleteRoles(String id, UserRoleRequest userRoleRequest);
}
