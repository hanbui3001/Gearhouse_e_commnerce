package com.example.demo_ecommerce.repository;

import com.example.demo_ecommerce.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
    long deleteByExpiredTimeBefore(Date now);
}
