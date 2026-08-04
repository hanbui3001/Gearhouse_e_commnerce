package com.example.demo_ecommerce.security;

import com.example.demo_ecommerce.model.User;
import com.example.demo_ecommerce.service.OAuth2AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class Oauth2LoginHandler implements AuthenticationSuccessHandler {
    private final OAuth2AuthenticationService oAuth2AuthenticationService;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${client.frontend-url:http://localhost:3000}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        User user = oAuth2AuthenticationService.loginWithGoogle(oAuth2User);

        String loginCode = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(
                "oauth-login:" + loginCode,
                user.getId(),
                Duration.ofSeconds(60)
        );

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        String encodedCode = URLEncoder.encode(loginCode, StandardCharsets.UTF_8);
        response.sendRedirect(frontendUrl + "/oauth/success?code=" + encodedCode);
    }
}
