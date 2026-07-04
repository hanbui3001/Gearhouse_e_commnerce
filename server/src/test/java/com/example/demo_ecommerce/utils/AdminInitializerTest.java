package com.example.demo_ecommerce.utils;

import com.example.demo_ecommerce.enums.RoleName;
import com.example.demo_ecommerce.enums.Status;
import com.example.demo_ecommerce.model.Role;
import com.example.demo_ecommerce.model.User;
import com.example.demo_ecommerce.repository.UserRepository;
import com.example.demo_ecommerce.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminInitializerTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;

    private AdminInitializer adminInitializer;

    @BeforeEach
    void setUp() {
        adminInitializer = new AdminInitializer(
                userRepository,
                roleService,
                passwordEncoder
        );
        ReflectionTestUtils.setField(adminInitializer, "email", "admin@example.com");
        ReflectionTestUtils.setField(adminInitializer, "password", "secret");
        ReflectionTestUtils.setField(adminInitializer, "phoneNumber", "0900000000");
    }

    @Test
    void runCreatesAdminUserAndAssignsAdminRoleWhenMissing() throws Exception {
        Role adminRole = Role.builder()
                .name(RoleName.ROLE_ADMIN.name())
                .description("Admin role")
                .build();

        when(roleService.findRoleByNameOrCreate(RoleName.ROLE_ADMIN)).thenReturn(adminRole);
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("secret")).thenReturn("encoded-secret");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        adminInitializer.run(new DefaultApplicationArguments());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(2)).save(userCaptor.capture());
        User savedAdmin = userCaptor.getAllValues().getLast();
        assertThat(savedAdmin.getEmail()).isEqualTo("admin@example.com");
        assertThat(savedAdmin.getPassword()).isEqualTo("encoded-secret");
        assertThat(savedAdmin.getPhoneNumber()).isEqualTo("0900000000");
        assertThat(savedAdmin.getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(savedAdmin.getUserRoles())
                .singleElement()
                .satisfies(userRole -> assertThat(userRole.getRole()).isSameAs(adminRole));
    }

    @Test
    void runAssignsAdminRoleToExistingAdmin() throws Exception {
        Role adminRole = Role.builder()
                .name(RoleName.ROLE_ADMIN.name())
                .description("Admin role")
                .build();
        User existingAdmin = User.builder()
                .id("admin-id")
                .email("admin@example.com")
                .build();

        when(roleService.findRoleByNameOrCreate(RoleName.ROLE_ADMIN)).thenReturn(adminRole);
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(existingAdmin));

        adminInitializer.run(new DefaultApplicationArguments());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue()).isSameAs(existingAdmin);
        assertThat(userCaptor.getValue().getUserRoles())
                .singleElement()
                .satisfies(userRole -> assertThat(userRole.getRole()).isSameAs(adminRole));
    }
}
