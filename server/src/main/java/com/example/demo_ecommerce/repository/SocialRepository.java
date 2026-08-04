package com.example.demo_ecommerce.repository;

import com.example.demo_ecommerce.enums.AuthProvider;
import com.example.demo_ecommerce.model.SocialAccount;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialRepository extends JpaRepository<SocialAccount, String> {

    @EntityGraph(attributePaths = "user")
    Optional<SocialAccount> findByProviderAndProviderId(AuthProvider provider, String providerId);

    Optional<SocialAccount> findByUser_IdAndProvider(String userId, AuthProvider provider);
}
