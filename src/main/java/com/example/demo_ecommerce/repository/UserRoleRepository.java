package com.example.demo_ecommerce.repository;

import com.example.demo_ecommerce.model.Role;
import com.example.demo_ecommerce.model.User;
import com.example.demo_ecommerce.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    boolean existsByUserAndRole(User user, Role role);
}
