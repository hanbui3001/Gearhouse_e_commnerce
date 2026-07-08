package com.example.demo_ecommerce.security;

import com.example.demo_ecommerce.dto.response.ApiResponse;
import com.example.demo_ecommerce.model.User;
import com.example.demo_ecommerce.service.OAuth2AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Oauth2LoginHandler implements AuthenticationSuccessHandler {
    private final OAuth2AuthenticationService oAuth2AuthenticationService;
    private final ObjectMapper objectMapper;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User  oAuth2User = (OAuth2User) authentication.getPrincipal();
        User user = oAuth2AuthenticationService.loginWithGoogle(oAuth2User);
        Map<String,Object> data = Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "fullName", user.getFullName(),
                "authProvider", user.getAuthProvider(),
                "providerId", user.getProviderId(),
                "avatarUrl", user.getAvatarUrl()
        );
        ApiResponse<Map<String, Object>> body = ApiResponse.<Map<String, Object>>builder()
                .code(200)
                .message("successfully")
                .data(data)
                .build();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
