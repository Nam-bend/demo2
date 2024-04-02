package com.example.demo2.service.mapping;

import com.example.demo2.dto.ProductRequest;
import com.example.demo2.dto.ProductResponse;
import com.example.demo2.entity.Product;

public class ProductMapping {
    public static Product dtoToEntity(ProductRequest dto) {
        Product entity = new Product();
        entity.setProductName(dto.getProductName());
        entity.setYear(dto.getYear());
        entity.setPrice(dto.getPrice());
        entity.setUrl(dto.getUrl());
        return entity;

        // chuyển dữ liệu từ dto sang Entity nhập dữ liệu
        // của entity(set) qua việc lấy dữ liệu từ dto (get)
        // rồi trả về entity với dữ liệu đã có
    }
    public static ProductResponse entityToDto(Product entity){
        ProductResponse dto = new ProductResponse();
        dto.setId(entity.getId());
        dto.setProductName(entity.getProductName());
        dto.setYear(entity.getYear());
        dto.setPrice(entity.getPrice());
        dto.setUrl(entity.getUrl());
        return dto ;
        //chuyển dữ liệu từ entity sang dto nhập dữ liệu vào dto(set)
        //qua việc lấy dữ liệu từ entity (get)
        // rồi trả về Response với đủ dữ liệu có từ entity

    }
}
