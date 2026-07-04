package com.example.demo_ecommerce.dto.internal;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtDetails {
        private String value;
        private String jwtId;
        private long expiryTime;
        private long secondsTtl;
}
