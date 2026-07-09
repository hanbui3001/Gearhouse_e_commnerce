package com.example.demo_ecommerce.service;

import com.example.demo_ecommerce.dto.request.CategoryRequest;
import com.example.demo_ecommerce.dto.request.UpdateCategoryRequest;
import com.example.demo_ecommerce.dto.response.CategoryResponse;
import com.example.demo_ecommerce.dto.response.PageResponse;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse getCategoryById(String id);

    PageResponse<CategoryResponse> getAllCategories(int page, int size, String name, String slug);

    CategoryResponse updateCategory(String categoryId, UpdateCategoryRequest request);

    void deleteCategory(String categoryId);
}
