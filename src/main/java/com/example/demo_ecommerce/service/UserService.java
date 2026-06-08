package com.example.demo_ecommerce.service;

import com.example.demo_ecommerce.dto.request.UserRegisterRequest;
import com.example.demo_ecommerce.dto.response.UserDetailResponse;
import com.example.demo_ecommerce.model.User;

public interface UserService {
    UserDetailResponse registerUser(UserRegisterRequest request) throws Exception;
}
