package com.example.demo_ecommerce.dto.response;

import com.example.demo_ecommerce.model.ProductImage;

import java.util.List;

public record ProductResponse(
        String name,
        String description,
        Float price,
        int stock,
        String categoryId,
        List<ProductImage> images
) {
}
