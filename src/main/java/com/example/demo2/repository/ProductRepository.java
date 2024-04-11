package com.example.demo2.repository;

import com.example.demo2.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product ,Long> {

    Page<Product> findByProductNameContainingIgnoreCase(String filter, Pageable pageable);



}
