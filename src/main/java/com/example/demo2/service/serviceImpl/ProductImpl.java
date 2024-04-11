package com.example.demo2.service.serviceImpl;

import com.example.demo2.dto.ProductRequest;
import com.example.demo2.dto.ProductResponse;
import com.example.demo2.entity.Product;
import com.example.demo2.exception.CustomException;
import com.example.demo2.exception.ProductNotFoundException;
import com.example.demo2.repository.ProductRepository;
import com.example.demo2.service.ProductService;
import com.example.demo2.service.mapping.ProductMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductImpl implements ProductService {
    private final ProductRepository productRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductImpl.class);

    public ProductImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> product = productRepository.findAll();
        if (product.isEmpty()) {
            throw new ProductNotFoundException("No product found");
        }
        List<ProductResponse> responses = new ArrayList<>();
        for (Product products : product) {
            ProductResponse productResponse = ProductMapping.entityToDto(products);
            responses.add(productResponse);
        }
        return responses;

        // hoặc có thể dùng stream
        //  product.stream().map(ProductMapping::entityToDto).collect(Collectors.toList());
    }

    @Override
    public Page<ProductResponse> getAllProductsPaged(Pageable pageable) {
        LOGGER.info("== START Fetching all products with pagination ==");
        try {
            Page<Product> productsPage = productRepository.findAll(pageable);
            List<ProductResponse> responseList = new ArrayList<>();
            for (Product product : productsPage.getContent()) {
                ProductResponse response = ProductMapping.entityToDto(product);
                responseList.add(response);
            }
            LOGGER.info("== SUCCESS Fetching and mapping all products ==");
            return new PageImpl<>(responseList, pageable, productsPage.getTotalElements());
        } catch (Exception e) {
            LOGGER.error("== ERROR Failed to fetch products: {}", e.getMessage(), e);
            throw new CustomException("Failed to fetch products: " + e.getMessage());
        }
    }



    @Override
    public ProductResponse getProductById(Long id) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException("product not found by id" + id));
            return ProductMapping.entityToDto(product);
        } catch (Exception e) {
            // Xử lý lỗi khi lấy thông tin sản phẩm
            throw new CustomException("Error getting product: " + e.getMessage());
        }
    }

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        try {
            Product product = ProductMapping.dtoToEntity(productRequest);
            Product savedProduct = productRepository.save(product);
            return ProductMapping.entityToDto(savedProduct);
        } catch (Exception e) {
            throw new ProductNotFoundException("Error creating product: " + e.getMessage());
        }
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException("product not found by id" + id));
            // Cập nhật thông tin từ productRequest vào product
            // ...
            Product updatedProduct = productRepository.save(product);
            return ProductMapping.entityToDto(updatedProduct);
        } catch (Exception e) {
            throw new CustomException("Error updating product: " + e.getMessage());
        }
    }

    @Override
    public void deleteProduct(Long id) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException("product not found by id" + id));
            productRepository.delete(product);
        } catch (Exception e) {
            throw new CustomException("Error deleting product: " + e.getMessage());
        }
    }

}

