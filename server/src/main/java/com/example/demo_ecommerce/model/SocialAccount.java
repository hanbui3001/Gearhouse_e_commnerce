package com.example.demo_ecommerce.model;

import com.example.demo_ecommerce.enums.AuthProvider;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "social_accounts")
public class SocialAccount extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "provider", nullable = false)
    @Enumerated(EnumType.STRING)
    AuthProvider provider;

    @Column(name = "provider_id", nullable = false)
    String providerId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

}
