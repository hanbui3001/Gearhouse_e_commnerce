package com.example.demo_ecommerce.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(nullable = false)
    String name;
    String description;
    @Column(nullable = false)
    BigDecimal price;
    @Column(nullable = false)
    Integer stock;
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<ProductImage> productImages =  new ArrayList<>();

}
