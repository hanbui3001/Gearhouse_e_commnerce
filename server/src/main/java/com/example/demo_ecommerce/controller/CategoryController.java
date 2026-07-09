package com.example.demo_ecommerce.controller;

import com.example.demo_ecommerce.dto.request.CategoryRequest;
import com.example.demo_ecommerce.dto.request.UpdateCategoryRequest;
import com.example.demo_ecommerce.dto.response.ApiResponse;
import com.example.demo_ecommerce.dto.response.CategoryResponse;
import com.example.demo_ecommerce.dto.response.PageResponse;
import com.example.demo_ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/category")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/create")
    public ApiResponse<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
        var response = categoryService.createCategory(categoryRequest);
        return ApiResponse.<CategoryResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("create category successfully")
                .data(response)
                .build();
    }

    @GetMapping("/categories")
    public ApiResponse<PageResponse<CategoryResponse>> getCategories(@RequestParam(required = false, defaultValue = "1") int page,
                                                                     @RequestParam(required = false, defaultValue = "10") int size,
                                                                     @RequestParam(required = false, defaultValue = "") String name,
                                                                     @RequestParam(required = false, defaultValue = "") String slug) {
        var response = categoryService.getAllCategories(page, size, name, slug);
        return ApiResponse.<PageResponse<CategoryResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("get categories successfully")
                .data(response)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getCategoryById(@PathVariable String id) {
        var response = categoryService.getCategoryById(id);
        return ApiResponse.<CategoryResponse>builder()
                .code(HttpStatus.OK.value())
                .message("get category successfully")
                .data(response)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoryResponse> updateCategory(@PathVariable String id,
                                                        @RequestBody @Valid UpdateCategoryRequest categoryRequest) {
        var response = categoryService.updateCategory(id, categoryRequest);
        return ApiResponse.<CategoryResponse>builder()
                .code(HttpStatus.OK.value())
                .message("update category successfully")
                .data(response)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("delete category successfully")
                .build();
    }
}
