package com.example.demo_ecommerce.service;

import com.example.demo_ecommerce.dto.request.UserRegisterRequest;
import com.example.demo_ecommerce.dto.response.PageResponse;
import com.example.demo_ecommerce.dto.response.UserDetailResponse;
import com.example.demo_ecommerce.model.User;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;

public interface UserService {
    UserDetailResponse registerUser(UserRegisterRequest request);
    PageResponse<UserDetailResponse> getUsers(int page, int size, String email, String fullName, String phoneNumber);
}
