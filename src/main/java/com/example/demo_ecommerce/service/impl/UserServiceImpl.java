package com.example.demo_ecommerce.service.impl;

import com.example.demo_ecommerce.dto.request.ChangeStatusRequest;
import com.example.demo_ecommerce.dto.request.UserRegisterRequest;
import com.example.demo_ecommerce.dto.request.UserRoleRequest;
import com.example.demo_ecommerce.dto.request.UserUpdateRequest;
import com.example.demo_ecommerce.dto.response.PageResponse;
import com.example.demo_ecommerce.dto.response.UserDetailResponse;
import com.example.demo_ecommerce.dto.response.UserRoleResponse;
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
import com.example.demo_ecommerce.service.RoleService;
import com.example.demo_ecommerce.service.UserService;
import com.example.demo_ecommerce.utils.PageResponseUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    @Transactional
    public UserDetailResponse registerUser(UserRegisterRequest request) {
        Role roleByNameOrCreate = roleService.findRoleByNameOrCreate(RoleName.ROLE_USER);
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setStatus(Status.ACTIVE);
        user.addRole(roleByNameOrCreate);
        userRepository.save(user);
        return userMapper.toUserDetailResponse(user);

    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
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

    @Override
    public UserDetailResponse getMyProfile(Jwt jwt) {
        String id = jwt.getSubject();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return  userMapper.toUserDetailResponse(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserDetailResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserDetailResponse(user);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN') || #id == authentication.name")
    public UserDetailResponse updateUserById(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(request,user);
        userRepository.save(user);
        return userMapper.toUserDetailResponse(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserDetailResponse changeUserStatus(String id, ChangeStatusRequest status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.setStatus(status.status());
        userRepository.save(user);
        return userMapper.toUserDetailResponse(user);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserRoleResponse assignRoles(String id, UserRoleRequest userRoleRequest) {
        if(userRoleRequest.roles() == null|| userRoleRequest.roles().isEmpty()){
            throw new CustomException(ErrorCode.ROLE_REQUIRED);
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<String> roleName = userRoleRequest.roles()
                .stream()
                .distinct()
                .toList();
        List<Role> roles = roleRepository.findAllById(roleName);
        if(roles.size() != roleName.size()) {
            throw new CustomException(ErrorCode.ROLE_NOT_FOUND);
        }
        List<UserRole> userRoles = userRoleRepository.findByUser(user);
        Set<String> existRole = userRoles.stream()
                .map(userRole -> userRole.getRole().getName())
                .collect(Collectors.toSet());
        List<String> checkExistRole = roles.stream()
                .map(Role::getName)
                .filter(existRole::contains)
                .toList();
        if(!checkExistRole.isEmpty()) {
            throw new CustomException(ErrorCode.ROLE_EXISTED);
        }
        List<UserRole>  userRolesToAdd = roles.stream()
                .map(role -> UserRole.builder()
                        .user(user)
                        .role(role)
                        .build())
                .toList();
        if(!userRolesToAdd.isEmpty()) {
            userRoleRepository.saveAll(userRolesToAdd);
        }
        List<String> finalRoles = Stream.concat(userRoles.stream()
                .map(userRole -> userRole.getRole().getName()),
                userRolesToAdd.stream()
                        .map(userRole -> userRole.getRole().getName()))
                .distinct().toList();
        return UserRoleResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .roles(finalRoles)
                .build();

    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserRoleResponse deleteRoles(String id, UserRoleRequest userRoleRequest) {
        if(userRoleRequest.roles() == null|| userRoleRequest.roles().isEmpty()){
            throw new CustomException(ErrorCode.ROLE_REQUIRED);
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<String> roleRequest = userRoleRequest.roles()
                .stream()
                .distinct()
                .toList();
        List<Role> roles = roleRepository.findAllById(roleRequest);
        if(roles.size() != roleRequest.size()) {
            throw new CustomException(ErrorCode.ROLE_NOT_FOUND);
        }
        List<UserRole> userRoles = userRoleRepository.findByUser(user);
        boolean isHaveAdminRole = userRoles.stream()
                .anyMatch(role -> role.getRole().getName().equals(RoleName.ROLE_ADMIN.name()));
        boolean isRequestHasAdminRole = roleRequest.contains(RoleName.ROLE_ADMIN.name());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isSelfRevokeAdmin = authentication != null
                && user.getId().equals(authentication.getName())
                && isHaveAdminRole
                && isRequestHasAdminRole;
        if(isSelfRevokeAdmin) {
            throw new CustomException(ErrorCode.CANNOT_SELF_REVOKE_ADMIN);
        }
        Set<String> roleNamesToDelete = roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        List<UserRole> userRolesToDelete = userRoles.stream()
                .filter(userRole -> roleNamesToDelete.contains(userRole.getRole().getName()))
                .toList();
        if(userRolesToDelete.size() != roleRequest.size()) {
            throw new CustomException(ErrorCode.ROLE_NOT_ASSIGN_TO_USER);
        }
        userRoleRepository.deleteAll(userRolesToDelete);
        List<String> finalRoles = userRoles.stream()
                .map(userRole -> userRole.getRole().getName())
                .filter(roleName -> !roleNamesToDelete.contains(roleName))
                .toList();
        return UserRoleResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .roles(finalRoles)
                .build();

    }


}
