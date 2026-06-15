package com.example.demo_ecommerce.service;

import com.example.demo_ecommerce.enums.RoleName;
import com.example.demo_ecommerce.model.Role;

public interface RoleService {
    Role findRoleByNameOrCreate(RoleName roleName);
}
