package com.example.demo_ecommerce.utils;

import com.example.demo_ecommerce.model.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public final class SecurityUtil {
    public static List<String> getAuthorities(User user){
        return user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }
}
