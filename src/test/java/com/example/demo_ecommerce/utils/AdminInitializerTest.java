package com.example.demo_ecommerce.utils;

import com.example.demo_ecommerce.enums.RoleName;
import com.example.demo_ecommerce.enums.Status;
import com.example.demo_ecommerce.model.Role;
import com.example.demo_ecommerce.model.User;
import com.example.demo_ecommerce.model.UserRole;
import com.example.demo_ecommerce.repository.RoleRepository;
import com.example.demo_ecommerce.repository.UserRepository;
import com.example.demo_ecommerce.repository.UserRoleRepository;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminInitializerTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private AdminInitializer adminInitializer;

    @BeforeEach
    void setUp() {
        adminInitializer = new AdminInitializer(
                userRepository,
                roleRepository,
                userRoleRepository,
                passwordEncoder
        );
        ReflectionTestUtils.setField(adminInitializer, "email", "admin@example.com");
        ReflectionTestUtils.setField(adminInitializer, "password", "secret");
        ReflectionTestUtils.setField(adminInitializer, "phoneNumber", "0900000000");
    }

    @Test
    void runCreatesAdminRoleUserAndRoleAssignmentWhenMissing() throws Exception {
        Role savedAdminRole = Role.builder()
                .name(RoleName.ROLE_ADMIN.name())
                .description("Admin role")
                .build();
        User savedAdmin = User.builder()
                .id("admin-id")
                .email("admin@example.com")
                .password("encoded-secret")
                .phoneNumber("0900000000")
                .fullName("admin")
                .status(Status.ACTIVE)
                .build();

        when(roleRepository.findByName(RoleName.ROLE_ADMIN.name())).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenReturn(savedAdminRole);
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("secret")).thenReturn("encoded-secret");
        when(userRepository.save(any(User.class))).thenReturn(savedAdmin);
        when(userRoleRepository.existsByUserAndRole(savedAdmin, savedAdminRole)).thenReturn(false);

        adminInitializer.run(new DefaultApplicationArguments());

        ArgumentCaptor<Role> roleCaptor = ArgumentCaptor.forClass(Role.class);
        verify(roleRepository).save(roleCaptor.capture());
        assertThat(roleCaptor.getValue().getName()).isEqualTo(RoleName.ROLE_ADMIN.name());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getEmail()).isEqualTo("admin@example.com");
        assertThat(userCaptor.getValue().getPassword()).isEqualTo("encoded-secret");
        assertThat(userCaptor.getValue().getPhoneNumber()).isEqualTo("0900000000");
        assertThat(userCaptor.getValue().getStatus()).isEqualTo(Status.ACTIVE);

        ArgumentCaptor<UserRole> userRoleCaptor = ArgumentCaptor.forClass(UserRole.class);
        verify(userRoleRepository).save(userRoleCaptor.capture());
        assertThat(userRoleCaptor.getValue().getUser()).isSameAs(savedAdmin);
        assertThat(userRoleCaptor.getValue().getRole()).isSameAs(savedAdminRole);
    }

    @Test
    void runDoesNotCreateDuplicatesWhenAdminAlreadyHasRole() throws Exception {
        Role existingAdminRole = Role.builder()
                .name(RoleName.ROLE_ADMIN.name())
                .description("Admin role")
                .build();
        User existingAdmin = User.builder()
                .id("admin-id")
                .email("admin@example.com")
                .build();

        when(roleRepository.findByName(RoleName.ROLE_ADMIN.name())).thenReturn(Optional.of(existingAdminRole));
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(existingAdmin));
        when(userRoleRepository.existsByUserAndRole(existingAdmin, existingAdminRole)).thenReturn(true);

        adminInitializer.run(new DefaultApplicationArguments());

        verify(roleRepository, never()).save(any(Role.class));
        verify(userRepository, never()).save(any(User.class));
        verify(userRoleRepository, never()).save(any(UserRole.class));
    }
}
