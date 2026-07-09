package com.example.demo_ecommerce.service.impl;

import com.example.demo_ecommerce.dto.internal.GoogleInfoDetails;
import com.example.demo_ecommerce.enums.AuthProvider;
import com.example.demo_ecommerce.enums.RoleName;
import com.example.demo_ecommerce.enums.Status;
import com.example.demo_ecommerce.exception.CustomException;
import com.example.demo_ecommerce.exception.ErrorCode;
import com.example.demo_ecommerce.model.Role;
import com.example.demo_ecommerce.model.User;
import com.example.demo_ecommerce.repository.UserRepository;
import com.example.demo_ecommerce.service.OAuth2AuthenticationService;
import com.example.demo_ecommerce.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuth2AuthenticationServiceImpl implements OAuth2AuthenticationService {
    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.of(1970, 1, 1);

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User loginWithGoogle(OAuth2User oAuth2User) {
        GoogleInfoDetails googleInfo = toGoogleInfo(oAuth2User);
        if (!googleInfo.verified()) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return userRepository.findByAuthProviderAndProviderIdOrEmail(
                        AuthProvider.GOOGLE,
                        googleInfo.providerId(),
                        googleInfo.email())
                .map(user -> updateGoogleUser(user, googleInfo))
                .orElseGet(() -> createGoogleUser(googleInfo));
    }

    private GoogleInfoDetails toGoogleInfo(OAuth2User oAuth2User) {
        String providerId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        Boolean verified = oAuth2User.getAttribute("email_verified");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        if (!StringUtils.hasText(providerId) || !StringUtils.hasText(email)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return GoogleInfoDetails.builder()
                .providerId(providerId)
                .email(email)
                .verified(Boolean.TRUE.equals(verified))
                .name(StringUtils.hasText(name) ? name : email)
                .picture(picture)
                .build();
    }

    private User updateGoogleUser(User user, GoogleInfoDetails googleInfo) {
        user.setAuthProvider(AuthProvider.GOOGLE);
        user.setProviderId(googleInfo.providerId());
        user.setAvatarUrl(googleInfo.picture());
        user.setFullName(googleInfo.name());
        user.setStatus(Status.ACTIVE);

        if (!StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        }
        if (!StringUtils.hasText(user.getPhoneNumber())) {
            user.setPhoneNumber(defaultGooglePhoneNumber(googleInfo.providerId()));
        }
        if (user.getDateOfBirth() == null) {
            user.setDateOfBirth(DEFAULT_DATE_OF_BIRTH);
        }

        return userRepository.save(user);
    }

    private User createGoogleUser(GoogleInfoDetails googleInfo) {
        Role userRole = roleService.findRoleByNameOrCreate(RoleName.ROLE_USER);
        User user = User.builder()
                .email(googleInfo.email())
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .fullName(googleInfo.name())
                .phoneNumber(defaultGooglePhoneNumber(googleInfo.providerId()))
                .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
                .status(Status.ACTIVE)
                .authProvider(AuthProvider.GOOGLE)
                .providerId(googleInfo.providerId())
                .avatarUrl(googleInfo.picture())
                .build();
        user.addRole(userRole);
        return userRepository.save(user);
    }

    private String defaultGooglePhoneNumber(String providerId) {
        return "GOOGLE_" + providerId;
    }
}
