package com.example.demo_ecommerce.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PageResponse<T>(
        int currentPage,
        int pageSize,
        int totalPages,
        long total,
        List<T> data

) {
}
