package com.example.demo_ecommerce.mapper;

import com.example.demo_ecommerce.dto.request.UserRegisterRequest;
import com.example.demo_ecommerce.dto.response.UserDetailResponse;
import com.example.demo_ecommerce.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.context.annotation.ComponentScan;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    User toUser(UserRegisterRequest request);
    UserDetailResponse toUserDetailResponse(User user);
}
