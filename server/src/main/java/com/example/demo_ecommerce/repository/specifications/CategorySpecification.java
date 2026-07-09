package com.example.demo_ecommerce.repository.specifications;

import com.example.demo_ecommerce.model.Category;
import org.springframework.data.jpa.domain.PredicateSpecification;

public class CategorySpecification {
    public static PredicateSpecification<Category> hasName(String name) {
        return (from, criteriaBuilder) -> {
            if (name == null || name.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(from.get("name"), name);
        };
    }

    public static PredicateSpecification<Category> hasSlug(String slug) {
        return (from, criteriaBuilder) -> {
            if (slug == null || slug.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(from.get("slug"), slug);
        };
    }
}
