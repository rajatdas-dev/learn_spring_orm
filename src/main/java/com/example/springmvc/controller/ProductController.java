package com.example.springmvc.controller;

import com.example.springmvc.dto.request.product.ProductRequestDTO;
import com.example.springmvc.dto.request.product.ProductUpdateRequestDTO;
import com.example.springmvc.dto.request.product.ProductVariantRequestDTO;
import com.example.springmvc.dto.request.product.ProductVariantUpdateRequestDTO;
import com.example.springmvc.dto.response.product.ProductResponseDTO;
import com.example.springmvc.dto.response.product.ProductVariantResponseDTO;
import com.example.springmvc.entity.ProductVariant;
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

    @PostMapping("/{productId}/variants")
    public ResponseEntity<ApiResponse<ProductVariantResponseDTO>> createProductVariant(
            @PathVariable("productId") Long id,
            @RequestBody ProductVariantRequestDTO productVariantRequestDTO
            ){

        ProductVariantResponseDTO productVariantResponseDTO = productService.createProductVariant(id,productVariantRequestDTO);

        return ResponseEntity.ok()
                .body(
                        ApiResponse.success(
                                "Product Variant created successfully",
                                productVariantResponseDTO
                        )
                );
    }

    @GetMapping("/{productId}/variants")
    public ResponseEntity<ApiResponse<List<ProductVariantResponseDTO>>> getAllProductVariantsById(
            @PathVariable("productId") Long id
    ){
        List<ProductVariantResponseDTO> productVariantResponseDTOS = productService.getAllProductVariantsById(id);

        return  ResponseEntity.ok()
                .body(
                        ApiResponse.success(
                                "Product Variants are successfully fetched",
                                productVariantResponseDTOS
                        )
                );
    }

    @GetMapping("/variants/{variantId}")
    public ResponseEntity<ApiResponse<ProductVariantResponseDTO>> getProductVariantById(
            @PathVariable("variantId") Long id
    ){
        ProductVariantResponseDTO productVariantResponseDTO = productService.getProductVariantById(id);

        return ResponseEntity.ok()
                .body(
                        ApiResponse.success(
                                "Product Variant got successfully fetched !!",
                                productVariantResponseDTO
                        )
                );
    }

    @PutMapping("/variants/{variantId}")
    public ResponseEntity<ApiResponse<ProductVariantResponseDTO>> updateProductVariant(
            @PathVariable("variantId") Long id,
            @RequestBody ProductVariantUpdateRequestDTO productVariantUpdateRequestDTO
            ){

        ProductVariantResponseDTO productVariantResponseDTO =
                productService.updateProductVariant(id, productVariantUpdateRequestDTO);

        return ResponseEntity.ok()
                .body(
                        ApiResponse.success(
                                "Product Variant got successfully Updated !!",
                                productVariantResponseDTO
                        )
                );
    }

    @DeleteMapping("/variants/{variantId}")
    public ResponseEntity<ApiResponse<Void>> deleteProductVariant(@PathVariable("variantId") Long id){
        productService.deleteProductVariant(id);

        return ResponseEntity.ok()
                .body(ApiResponse.success("Product Variant got successfully deleted"));
    }
}
