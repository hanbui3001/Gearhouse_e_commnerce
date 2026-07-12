package com.example.demo_ecommerce.dto.request;

public record CreateCategoryRequest(
        String name,
        String slug
) {
}
