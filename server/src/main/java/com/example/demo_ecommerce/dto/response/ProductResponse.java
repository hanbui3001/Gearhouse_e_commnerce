package com.example.demo_ecommerce.dto.response;

import com.example.demo_ecommerce.enums.Status;
import com.example.demo_ecommerce.model.ProductImage;

import java.util.List;
import java.util.Map;

public record ProductResponse(
        String id,
        String name,
        String slug,
        String description,
        String categoryId,
        String brandId,
        Map<String, Object> specifications,
        Status status,
        List<ProductImage> images
) {
}
