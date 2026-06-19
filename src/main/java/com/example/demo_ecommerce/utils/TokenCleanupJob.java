package com.example.demo_ecommerce.utils;

import com.example.demo_ecommerce.repository.TokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenCleanupJob {
    private final TokenRepository tokenRepository;

    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Ho_Chi_Minh")
    @Transactional
    public void deleteExpiredTokens() {
        tokenRepository.deleteByExpiredTimeBefore(new Date());
    }
}
