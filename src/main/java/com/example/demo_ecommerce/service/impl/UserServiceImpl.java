package com.example.demo_ecommerce.service.impl;

import com.example.demo_ecommerce.dto.request.UserRegisterRequest;
import com.example.demo_ecommerce.dto.response.PageResponse;
import com.example.demo_ecommerce.dto.response.UserDetailResponse;
import com.example.demo_ecommerce.enums.RoleName;
import com.example.demo_ecommerce.enums.Status;
import com.example.demo_ecommerce.exception.CustomException;
import com.example.demo_ecommerce.exception.ErrorCode;
import com.example.demo_ecommerce.mapper.UserMapper;
import com.example.demo_ecommerce.model.Role;
import com.example.demo_ecommerce.model.User;
import com.example.demo_ecommerce.model.UserRole;
import com.example.demo_ecommerce.repository.RoleRepository;
import com.example.demo_ecommerce.repository.UserRepository;
import com.example.demo_ecommerce.repository.UserRoleRepository;
import com.example.demo_ecommerce.repository.specifications.UserSpecification;
import com.example.demo_ecommerce.service.UserService;
import com.example.demo_ecommerce.utils.PageResponseUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDetailResponse registerUser(UserRegisterRequest request) {
        if(userRepository.existsByEmail(request.email())){
            throw new CustomException(ErrorCode.USER_EXISTED);
        }
        if(userRepository.existsByPhoneNumber(request.phoneNumber())){
            throw new CustomException(ErrorCode.USER_EXISTED);
        }
        Role role = roleRepository.findByName(RoleName.ROLE_USER.name())
                .orElseGet(() ->{
                    Role userRole = Role.builder()
                            .name(RoleName.ROLE_USER.name())
                            .description("User role ")
                            .build();
                    return roleRepository.save(userRole);
                });
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
        UserRole userRole = UserRole.builder()
                .user(user)
                .role(role)
                .build();
        userRoleRepository.save(userRole);
        return userMapper.toUserDetailResponse(user);

    }

    @Override
    public PageResponse<UserDetailResponse> getUsers(int page, int size, String email, String fullName, String phoneNumber) {
        page = PageResponseUtils.normalizePage(page);
        size = PageResponseUtils.normalizeSize(size);
        Specification<User> userSpecification = Specification.where(UserSpecification.hasEmail(email))
                .and(UserSpecification.hasFullName(fullName))
                .and(UserSpecification.hasPhoneNumber(phoneNumber));
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("fullName").ascending());
        Page<User> users = userRepository.findAll(userSpecification, pageable);
        List<UserDetailResponse> userDetailResponses =users.getContent().stream()
                .map(userMapper::toUserDetailResponse)
                .toList();
        return PageResponse.<UserDetailResponse>builder()
                .currentPage(users.getNumber() + 1)
                .pageSize(users.getSize())
                .totalPages(users.getTotalPages())
                .total(users.getTotalElements())
                .data(userDetailResponses)
                .build();
    }

}
