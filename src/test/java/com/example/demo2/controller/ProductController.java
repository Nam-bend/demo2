package com.example.demo2.controller;

import com.example.demo2.dto.ProductResponse;
import com.example.demo2.entity.ResponseObject;
import com.example.demo2.exception.CustomException;
import com.example.demo2.exception.ProductNotFoundException;
import com.example.demo2.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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
