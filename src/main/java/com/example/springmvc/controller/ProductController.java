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

    @GetMapping("/page")
    public ResponseEntity<ApiResponse<org.springframework.data.domain.Page<ProductResponseDTO>>> getProductsPaged(
            @org.springframework.data.web.PageableDefault(page = 0, size = 10, sort = "name") org.springframework.data.domain.Pageable pageable) {
        org.springframework.data.domain.Page<ProductResponseDTO> paged = productService.getProductsPaged(pageable);
        return ResponseEntity.ok(ApiResponse.success("Paged products fetched successfully", paged));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<org.springframework.data.domain.Page<ProductResponseDTO>>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long vendorId,
            @RequestParam(required = false) java.math.BigDecimal minPrice,
            @RequestParam(required = false) java.math.BigDecimal maxPrice,
            @RequestParam(required = false) Boolean inStock,
            @org.springframework.data.web.PageableDefault(page = 0, size = 10) org.springframework.data.domain.Pageable pageable) {
        org.springframework.data.domain.Page<ProductResponseDTO> results = productService.searchProducts(keyword, categoryId, vendorId, minPrice, maxPrice, inStock, pageable);
        return ResponseEntity.ok(ApiResponse.success("Search results fetched successfully", results));
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

    @GetMapping("/{id}/details")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> getProductWithDetails(@PathVariable Long id) {
        ProductResponseDTO productResponseDTO = productService.getProductWithDetails(id);
        return ResponseEntity.ok(ApiResponse.success("Product with details fetched (JOIN FETCH)", productResponseDTO));
    }

    @GetMapping("/{id}/async")
    public java.util.concurrent.CompletableFuture<ResponseEntity<ApiResponse<ProductResponseDTO>>> getProductAsync(@PathVariable Long id) {
        return productService.getProductAsync(id)
                .thenApply(dto -> ResponseEntity.ok(ApiResponse.success("Product fetched asynchronously via CompletableFuture", dto)));
    }

    @GetMapping("/price-range")
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> getProductsByPriceRange(
            @RequestParam java.math.BigDecimal minPrice,
            @RequestParam java.math.BigDecimal maxPrice) {
        List<ProductResponseDTO> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(ApiResponse.success("Products by price range fetched", products));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> getLowStockProducts(
            @RequestParam(defaultValue = "5") Integer threshold) {
        List<ProductResponseDTO> products = productService.getLowStockProducts(threshold);
        return ResponseEntity.ok(ApiResponse.success("Low stock products fetched (Native Query)", products));
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
