package com.example.demo_ecommerce.configuration;

import com.example.demo_ecommerce.enums.Token;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

public class CustomOauth2TokenValidator implements OAuth2TokenValidator<Jwt> {
    OAuth2Error error = new OAuth2Error("INVALID_JWT_TOKEN", "Only Access Token Is Allowed", null);

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        Token token = Token.valueOf(jwt.getClaimAsString("typ"));
        if(token == Token.ACCESS){
            return OAuth2TokenValidatorResult.success();
        }
        return OAuth2TokenValidatorResult.failure(error);
    }
}