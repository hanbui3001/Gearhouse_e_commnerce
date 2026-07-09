package com.example.demo_ecommerce.service.impl;

import com.example.demo_ecommerce.dto.request.CategoryRequest;
import com.example.demo_ecommerce.dto.request.UpdateCategoryRequest;
import com.example.demo_ecommerce.dto.response.CategoryResponse;
import com.example.demo_ecommerce.dto.response.PageResponse;
import com.example.demo_ecommerce.exception.CustomException;
import com.example.demo_ecommerce.exception.ErrorCode;
import com.example.demo_ecommerce.mapper.CategoryMapper;
import com.example.demo_ecommerce.model.Category;
import com.example.demo_ecommerce.repository.CategoryRepository;
import com.example.demo_ecommerce.repository.specifications.CategorySpecification;
import com.example.demo_ecommerce.service.CategoryService;
import com.example.demo_ecommerce.utils.PageResponseUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.name())) {
            throw new CustomException(ErrorCode.CATEGORY_EXISTED);
        }
        Category category = Category.builder()
                .name(request.name())
                .slug(request.slug())
                .build();
        categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse getCategoryById(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public PageResponse<CategoryResponse> getAllCategories(int page, int size, String name, String slug) {
        page = PageResponseUtils.normalizePage(page);
        size = PageResponseUtils.normalizeSize(size);
        Specification<Category> specification = Specification.where(CategorySpecification.hasName(name)
                .and(CategorySpecification.hasSlug(slug)));
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("name").ascending());
        Page<Category> categoryPage = categoryRepository.findAll(specification, pageable);
        List<CategoryResponse> categoryResponseList = categoryPage.getContent().stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
        return PageResponse.<CategoryResponse>builder()
                .currentPage(categoryPage.getNumber() + 1)
                .pageSize(categoryPage.getSize())
                .totalPages(categoryPage.getTotalPages())
                .total(categoryPage.getTotalElements())
                .data(categoryResponseList)
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse updateCategory(String categoryId, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        categoryMapper.updateCategory(request, category);
        categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);
    }

    @Override
    public void deleteCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        categoryRepository.delete(category);
    }

}
