package com.example.demo_ecommerce.dto.request;

import com.example.demo_ecommerce.enums.RoleName;
import lombok.Builder;

import java.util.List;

@Builder
public record UserRoleRequest(
        List<String> roles
) {
}
