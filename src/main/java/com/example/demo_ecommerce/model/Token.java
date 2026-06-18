package com.example.demo_ecommerce.model;

import com.example.demo_ecommerce.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Token extends BaseEntity {
    @Id
    String jwtId;

    String userId;

    @Enumerated(EnumType.STRING)
    TokenType tokenType;

    Date expiredTime;

    boolean revoked;

}
