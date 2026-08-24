package com.example.springmvc.controller;

import com.example.springmvc.dto.request.product.ProductRequestDTO;
import com.example.springmvc.dto.request.product.ProductUpdateRequestDTO;
import com.example.springmvc.dto.response.ProductResponseDTO;
import com.example.springmvc.response.ApiResponse;
import com.example.springmvc.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> getAllProducts(){

        List<ProductResponseDTO> productResponseDTOList = productService.getAllProducts();

        return ResponseEntity.ok()
                .body(
                        ApiResponse.success(
                                "All Products fetched",
                                productResponseDTOList
                        )
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> getProductById(@PathVariable Long id){

        ProductResponseDTO productResponseDTO = productService.getById(id);

        return ResponseEntity.ok()
                .body(
                        ApiResponse.success(
                                "Product got fetched",
                                productResponseDTO
                        )
                );
    }

    @PutMapping("/{id}")
    public  ResponseEntity<ApiResponse<ProductResponseDTO>> updateProductById(
            @PathVariable Long id,
            @RequestBody ProductUpdateRequestDTO productUpdateRequestDTO){

        ProductResponseDTO responseDTO = productService.updateProductsById(id, productUpdateRequestDTO);

        return ResponseEntity.ok()
                .body(
                        ApiResponse.success(
                                "Product Updated Successfully",
                                responseDTO
                        )
                );
    }
}
