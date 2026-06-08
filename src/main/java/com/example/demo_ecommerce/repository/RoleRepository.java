package com.example.demo_ecommerce.repository;

import com.example.demo_ecommerce.enums.Roles;
import com.example.demo_ecommerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(String role);
}
