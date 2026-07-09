package com.example.demo_ecommerce.mapper;

import com.example.demo_ecommerce.dto.request.UpdateCategoryRequest;
import com.example.demo_ecommerce.dto.response.CategoryResponse;
import com.example.demo_ecommerce.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {
    CategoryResponse toCategoryResponse(Category category);

    void updateCategory(UpdateCategoryRequest request, @MappingTarget Category category);
}
