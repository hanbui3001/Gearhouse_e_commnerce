package com.example.demo_ecommerce.service.impl;

import com.example.demo_ecommerce.enums.RoleName;
import com.example.demo_ecommerce.model.Role;
import com.example.demo_ecommerce.model.User;
import com.example.demo_ecommerce.repository.RoleRepository;
import com.example.demo_ecommerce.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    public Role findRoleByNameOrCreate(RoleName roleName) {
        return roleRepository.findByName(roleName.name())
                .orElseGet(() -> {
                    Role userRole = Role.builder()
                            .name(roleName.name())
                            .build();
                    return roleRepository.save(userRole);
                });
    }
}
