package com.example.demo_ecommerce.service;

import com.example.demo_ecommerce.model.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2AuthenticationService {
    User loginWithGoogle(OAuth2User oAuth2User);
}
