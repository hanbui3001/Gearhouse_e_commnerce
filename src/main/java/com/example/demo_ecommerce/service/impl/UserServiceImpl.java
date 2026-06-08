package com.example.demo_ecommerce.service.impl;

import com.example.demo_ecommerce.dto.request.UserRegisterRequest;
import com.example.demo_ecommerce.dto.response.UserDetailResponse;
import com.example.demo_ecommerce.enums.Roles;
import com.example.demo_ecommerce.enums.Status;
import com.example.demo_ecommerce.exception.CustomException;
import com.example.demo_ecommerce.exception.ErrorCode;
import com.example.demo_ecommerce.mapper.UserMapper;
import com.example.demo_ecommerce.model.Role;
import com.example.demo_ecommerce.model.User;
import com.example.demo_ecommerce.repository.RoleRepository;
import com.example.demo_ecommerce.repository.UserRepository;
import com.example.demo_ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetailResponse registerUser(UserRegisterRequest request) throws Exception {
        if(userRepository.existsByEmail(request.email())){
            throw new CustomException(ErrorCode.USER_EXISTED);
        }
        if(userRepository.existsByPhoneNumber(request.phoneNumber())){
            throw new CustomException(ErrorCode.USER_EXISTED);
        }
        Role userRole = roleRepository.findByName(Roles.ROLE_USER.name())
                .orElseThrow(() -> new CustomException(ErrorCode.ROLE_INVALID));
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setStatus(Status.ACTIVE);
        user.getRoles().add(userRole);
        userRepository.save(user);
        return userMapper.toUserDetailResponse(user);

    }

}
