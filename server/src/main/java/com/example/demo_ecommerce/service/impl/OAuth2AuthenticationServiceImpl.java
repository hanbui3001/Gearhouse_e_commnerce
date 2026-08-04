package com.example.demo_ecommerce.service.impl;

import com.example.demo_ecommerce.enums.AuthProvider;
import com.example.demo_ecommerce.enums.RoleName;
import com.example.demo_ecommerce.exception.CustomException;
import com.example.demo_ecommerce.exception.ErrorCode;
import com.example.demo_ecommerce.model.Role;
import com.example.demo_ecommerce.model.SocialAccount;
import com.example.demo_ecommerce.model.User;
import com.example.demo_ecommerce.repository.SocialRepository;
import com.example.demo_ecommerce.repository.TokenRepository;
import com.example.demo_ecommerce.repository.UserRepository;
import com.example.demo_ecommerce.service.JwtService;
import com.example.demo_ecommerce.service.OAuth2AuthenticationService;
import com.example.demo_ecommerce.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class OAuth2AuthenticationServiceImpl implements OAuth2AuthenticationService {

    private final SocialRepository socialRepository;
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    @Override
    @Transactional
    public User loginWithGoogle(OAuth2User oAuth2User) {

        String email = oAuth2User.getAttribute("email");
        String providerId = oAuth2User.getAttribute("sub");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");
        Boolean emailVerified = oAuth2User.getAttribute("email_verified");

        if (!Boolean.TRUE.equals(emailVerified)
                || !StringUtils.hasText(email)
                || !StringUtils.hasText(providerId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return socialRepository.findByProviderAndProviderId(AuthProvider.GOOGLE, providerId)
                .map(SocialAccount::getUser)
                .orElseGet(() -> findOrCreateAndLink(providerId, email, name, picture))
                ;
    }

    private User findOrCreateAndLink(String providerId, String email, String name, String picture) {

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createAccountGoogle(email, name, picture));
        socialRepository.findByUser_IdAndProvider(user.getId(), AuthProvider.GOOGLE)
                .ifPresent(account -> {
                    throw new CustomException(ErrorCode.SOCIAL_ACCOUNT_ALREADY_LINK);
                });

        SocialAccount socialAccount = SocialAccount.builder()
                .providerId(providerId)
                .provider(AuthProvider.GOOGLE)
                .user(user)
                .build();

        socialRepository.save(socialAccount);
        return user;
    }

    private User createAccountGoogle(String email, String name, String picture) {
        Role userRole = roleService.findRoleByNameOrCreate(
                RoleName.ROLE_USER
        );

        User user = User.builder()
                .email(email)
                .fullName(StringUtils.hasText(name) ? name : email)
                .avatarUrl(picture)
                .build();

        user.addRole(userRole);
        userRepository.save(user);
        return user;

    }
}
