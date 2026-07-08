package com.example.demo_ecommerce.repository;

import com.example.demo_ecommerce.enums.AuthProvider;
import com.example.demo_ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.userRoles ur " +
            "LEFT JOIN FETCH ur.role r" +
            " WHERE u.id = :id")
    Optional<User> findByIdWithRoles(String id);

    Optional<User> findByAuthProviderAndProviderId(AuthProvider authProvider, String providerId);
    Optional<User> findByAuthProviderAndProviderIdOrEmail(AuthProvider authProvider, String providerId, String email);
}
