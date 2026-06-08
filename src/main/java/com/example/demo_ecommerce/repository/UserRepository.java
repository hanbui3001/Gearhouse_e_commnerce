package com.example.demo_ecommerce.repository;

import com.example.demo_ecommerce.model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    boolean existsByPhoneNumber(@NotBlank(message = "phone number is required") String s);
}
