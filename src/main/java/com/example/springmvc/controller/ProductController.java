package com.example.springmvc.controller;

import com.example.springmvc.dto.request.ProductRequestDTO;
import com.example.springmvc.dto.response.ProductResponseDTO;
import com.example.springmvc.response.ApiResponse;
import com.example.springmvc.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/create-product")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO){

//         return  ResponseEntity.ok(productService.createProduct(productRequestDTO));
        ProductResponseDTO responseDTO = productService.createProduct(productRequestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Product Created Successfully",
                                responseDTO
                        )
                );
    }
}
