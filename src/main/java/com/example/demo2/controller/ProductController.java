package com.example.demo2.controller;

import com.example.demo2.dto.ProductResponse;
import com.example.demo2.entity.ResponseObject;
import com.example.demo2.exception.CustomException;
import com.example.demo2.exception.ProductNotFoundException;
import com.example.demo2.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/Products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getProductById(@PathVariable Long id) {
        try {
            ProductResponse product = productService.getProductById(id);
            ResponseObject responseObject = new ResponseObject("success", "Product found", product);
            return ResponseEntity.status(HttpStatus.OK).body(responseObject);
        } catch (ProductNotFoundException e) {
            ResponseObject responseObject = new ResponseObject("error", "Product not found", "");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseObject);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("error", "Internal Server Error", "")
            );
        }
    }

    @GetMapping("/paged")

    public ResponseEntity<ResponseObject> getAllProductsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(required = false) String filter
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductResponse> productResponses;

            if (filter != null && !filter.isEmpty()) {
                // Nếu có filter, gọi phương thức có lọc của ProductService
                productResponses = productService.getAllProductsFiltered(pageable, filter);
            } else {
                // Nếu không có filter, gọi phương thức không lọc của ProductService
                productResponses = productService.getAllProductsPaged(pageable);
            }
            if (productResponses == null || productResponses.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                        new ResponseObject("failed", "No products found", null));
            }

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("success", "Products found", productResponses.getContent()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ResponseObject("error", "Internal Server Error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            ResponseObject responseObject = new ResponseObject("success", "Product deleted successfully", null);
            return ResponseEntity.status(HttpStatus.OK).body(responseObject);
        } catch (ProductNotFoundException e) {
            ResponseObject responseObject = new ResponseObject("error", "Product not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseObject);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
