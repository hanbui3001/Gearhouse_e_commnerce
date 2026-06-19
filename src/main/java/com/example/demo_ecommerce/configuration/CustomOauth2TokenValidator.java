package com.example.demo_ecommerce.configuration;

import com.example.demo_ecommerce.enums.TokenType;
import com.example.demo_ecommerce.model.Token;
import com.example.demo_ecommerce.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomOauth2TokenValidator implements OAuth2TokenValidator<Jwt> {
    private final TokenRepository repository;
    OAuth2Error error = new OAuth2Error("INVALID_JWT_TOKEN", "Only Access Token Is Allowed", null);

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        String tokenType = jwt.getClaimAsString("typ");
        if(!TokenType.ACCESS.name().equals(tokenType)){
            return OAuth2TokenValidatorResult.failure(error);
        }
        String jwtId = jwt.getId();
        boolean isRevoked = repository.findById(jwtId)
                .map(Token::isRevoked)
                .orElse(false);
        if(isRevoked){
            return OAuth2TokenValidatorResult.failure(error);
        }
        return OAuth2TokenValidatorResult.success();
    }
}
