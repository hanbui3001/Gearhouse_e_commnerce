package com.example.demo_ecommerce.dto.request;

public record CreateProductRequest(
        String name,
        String slug
) {
}
