package com.example.demo_ecommerce.dto.request;

public record UpdateCategoryRequest(
        String name,
        String slug
) {
}
