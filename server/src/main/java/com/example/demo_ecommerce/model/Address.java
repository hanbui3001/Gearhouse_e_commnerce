package com.example.demo_ecommerce.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "addresses")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "receiver_name", nullable = false)
    String receiverName;

    @Column(name = "phone_number", nullable = false)
    String phoneNumber;

    @Column(name = "address_line", nullable = false)
    String addressLine;

    @Column(name = "ward", nullable = false)
    String ward;

    @Column(name = "district", nullable = false)
    String district;

    @Column(name = "province", nullable = false)
    String province;

    @Column(name = "is_default", nullable = false)
    @Builder.Default
    Boolean isDefault = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    User user;
}
