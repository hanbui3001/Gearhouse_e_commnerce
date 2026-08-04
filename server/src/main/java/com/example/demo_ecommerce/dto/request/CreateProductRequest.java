package com.example.demo_ecommerce.dto.request;

import com.example.demo_ecommerce.model.ProductImage;

import java.util.List;
import java.util.Map;

public record CreateProductRequest(
        String name,
        String slug,
        String description,
        String categoryId,
        String brandId,
        Map<String, Object> specifications,
        List<ProductImage> images
) {
}
