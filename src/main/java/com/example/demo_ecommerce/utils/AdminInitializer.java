package com.example.demo_ecommerce.utils;

import com.example.demo_ecommerce.enums.RoleName;
import com.example.demo_ecommerce.enums.Status;
import com.example.demo_ecommerce.model.Role;
import com.example.demo_ecommerce.model.User;
import com.example.demo_ecommerce.model.UserRole;
import com.example.demo_ecommerce.repository.RoleRepository;
import com.example.demo_ecommerce.repository.UserRepository;
import com.example.demo_ecommerce.repository.UserRoleRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder encoder;
    @Value("${web.admin.username}")
    private String email;
    @Value("${web.admin.password}")
    private String password;
    @Value("${web.admin.phone}")
    private String phoneNumber;

    @Override
    @Transactional
    public void run(@NonNull ApplicationArguments args) {
        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN.name())
                .orElseGet(() -> roleRepository.save(
                        Role.builder()
                                .name(RoleName.ROLE_ADMIN.name())
                                .description("Admin role")
                                .build()
                ));
        User admin = userRepository.findByEmail(email)
                .orElseGet(() -> {
                         User adminUser = User.builder()
                            .email(email)
                            .password(encoder.encode(password))
                            .phoneNumber(phoneNumber)
                            .fullName("admin")
                            .dateOfBirth(LocalDate.of(2000, 2, 1))
                            .status(Status.ACTIVE)
                            .build();
                         return userRepository.save(adminUser);
                });

        if (!userRoleRepository.existsByUserAndRole(admin, adminRole)) {
            userRoleRepository.save(UserRole.builder()
                    .user(admin)
                    .role(adminRole)
                    .build());
        }
    }
}
