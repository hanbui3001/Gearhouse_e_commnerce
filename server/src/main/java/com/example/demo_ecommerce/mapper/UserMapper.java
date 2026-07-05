package com.example.demo_ecommerce.mapper;

import com.example.demo_ecommerce.dto.request.CreateGoogleUserRequest;
import com.example.demo_ecommerce.dto.request.UserRegisterRequest;
import com.example.demo_ecommerce.dto.request.UserUpdateRequest;
import com.example.demo_ecommerce.dto.response.UserDetailResponse;
import com.example.demo_ecommerce.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    User toUser(UserRegisterRequest request);
    UserDetailResponse toUserDetailResponse(User user);
    User toUser(CreateGoogleUserRequest  request);
    void updateUser(UserUpdateRequest request, @MappingTarget User user);
}
