package com.example.demo_ecommerce.model;

import com.example.demo_ecommerce.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@RedisHash(value = "RedisToken")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Token{
    @Id
    String jwtId;
    String userId;
    @Enumerated(EnumType.STRING)
    TokenType tokenType;
    @TimeToLive(unit = TimeUnit.SECONDS)
    long timeToLive;
    boolean revoked;

}
