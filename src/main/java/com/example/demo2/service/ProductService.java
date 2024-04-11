package com.example.demo2.service;

import com.example.demo2.dto.ProductRequest;
import com.example.demo2.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ProductService {
    List<ProductResponse> getAllProducts();
    Page<ProductResponse> getAllProductsPaged(Pageable pageable);
    ProductResponse getProductById(Long id);
    ProductResponse createProduct(ProductRequest productRequest);
    ProductResponse updateProduct(Long id, ProductRequest productRequest);
    void deleteProduct(Long id);
}
