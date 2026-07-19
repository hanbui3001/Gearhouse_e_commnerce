package com.example.demo_ecommerce.service.impl;

import com.example.demo_ecommerce.dto.request.CreateProductRequest;
import com.example.demo_ecommerce.dto.response.ProductResponse;
import com.example.demo_ecommerce.exception.CustomException;
import com.example.demo_ecommerce.exception.ErrorCode;
import com.example.demo_ecommerce.model.Category;
import com.example.demo_ecommerce.model.Product;
import com.example.demo_ecommerce.model.ProductImage;
import com.example.demo_ecommerce.repository.CategoryRepository;
import com.example.demo_ecommerce.repository.ProductRepository;
import com.example.demo_ecommerce.service.ProduceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProduceService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    @CacheEvict(value = "product", key = "'all-product'")
    public ProductResponse createProduct(CreateProductRequest createProductRequest) {
        Category category = categoryRepository.findById(createProductRequest.categoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        Product product = new Product();
        product.setName(createProductRequest.name());
        product.setDescription(createProductRequest.description());
        product.setPrice(BigDecimal.valueOf(createProductRequest.price()));
        product.setStock(createProductRequest.stock());
        product.setCategory(category);

        List<ProductImage> images = new ArrayList<>();
        if (createProductRequest.images() != null) {
            images = createProductRequest.images().stream()
                    .map(imageRequest -> {
                        ProductImage productImage = new ProductImage();
                        productImage.setImageUrl(imageRequest.getImageUrl());
                        productImage.setDisplayOrder(imageRequest.getDisplayOrder());
                        productImage.setProduct(product);
                        return productImage;
                    })
                    .toList();
        }
        product.setProductImages(images);

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
                product.getName(),
                product.getDescription(),
                product.getPrice().floatValue(),
                product.getStock(),
                product.getCategory() != null ? product.getCategory().getId() : null,
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
