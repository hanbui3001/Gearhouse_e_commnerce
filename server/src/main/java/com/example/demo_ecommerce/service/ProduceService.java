package com.example.demo_ecommerce.service;

import com.example.demo_ecommerce.dto.request.CreateProductRequest;
import com.example.demo_ecommerce.dto.response.ProductResponse;

import java.util.List;

public interface ProduceService {
    ProductResponse createProduct(CreateProductRequest createProductRequest);

    List<ProductResponse> findAllProducts();
}
