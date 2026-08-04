package com.example.demo_ecommerce.service.impl;

import com.example.demo_ecommerce.dto.request.CreateProductRequest;
import com.example.demo_ecommerce.dto.response.ProductResponse;
import com.example.demo_ecommerce.exception.CustomException;
import com.example.demo_ecommerce.exception.ErrorCode;
import com.example.demo_ecommerce.model.Brand;
import com.example.demo_ecommerce.model.Category;
import com.example.demo_ecommerce.model.Product;
import com.example.demo_ecommerce.model.ProductImage;
import com.example.demo_ecommerce.repository.BrandRepository;
import com.example.demo_ecommerce.repository.CategoryRepository;
import com.example.demo_ecommerce.repository.ProductRepository;
import com.example.demo_ecommerce.service.ProduceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProduceService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    @Override
    @Transactional
    @CacheEvict(value = "product", key = "'all-product'")
    
    public ProductResponse createProduct(CreateProductRequest createProductRequest) {
        Category category = categoryRepository.findById(createProductRequest.categoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        Brand brand = brandRepository.findById(createProductRequest.brandId())
                .orElseThrow(() -> new CustomException(ErrorCode.BRAND_NOT_FOUND));

        Product product = new Product();
        product.setName(createProductRequest.name());
        product.setSlug(createProductRequest.slug());
        product.setDescription(createProductRequest.description());
        product.setCategory(category);
        product.setBrand(brand);
        product.setSpecifications(createProductRequest.specifications() == null
                ? new HashMap<>()
                : new HashMap<>(createProductRequest.specifications()));

        if (createProductRequest.images() != null) {
            createProductRequest.images().forEach(imageRequest -> {
                ProductImage productImage = new ProductImage();
                productImage.setImageUrl(imageRequest.getImageUrl());
                productImage.setDisplayOrder(imageRequest.getDisplayOrder() == null
                        ? 0
                        : imageRequest.getDisplayOrder());
                product.addProductImage(productImage);
            });
        }

        Product savedProduct = productRepository.save(product);
        return toProductResponse(savedProduct);
    }

    @Override
    @Transactional
    @Cacheable(value = "product", key = "'all-product'")
    public List<ProductResponse> findAllProducts() {
        return productRepository.findAll().stream()
                .map(this::toProductResponse)
                .toList();
    }

    private ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getDescription(),
                product.getCategory() != null ? product.getCategory().getId() : null,
                product.getBrand() != null ? product.getBrand().getId() : null,
                product.getSpecifications(),
                product.getStatus(),
                toProductImagesResponse(product.getProductImages())
        );
    }

    private List<ProductImage> toProductImagesResponse(List<ProductImage> productImages) {
        if (productImages == null) {
            return List.of();
        }

        return productImages.stream()
                .map(productImage -> new ProductImage(
                        productImage.getId(),
                        productImage.getImageUrl(),
                        productImage.getDisplayOrder(),
                        null
                ))
                .toList();
    }
}
