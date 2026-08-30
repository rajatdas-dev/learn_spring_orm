package com.example.springmvc.service;

import com.example.springmvc.dto.request.product.ProductRequestDTO;
import com.example.springmvc.dto.request.product.ProductUpdateRequestDTO;
import com.example.springmvc.dto.request.product.ProductVariantRequestDTO;
import com.example.springmvc.dto.request.product.ProductVariantUpdateRequestDTO;
import com.example.springmvc.dto.response.product.ProductResponseDTO;
import com.example.springmvc.dto.response.product.ProductVariantResponseDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ProductService {
     ProductResponseDTO createProduct(ProductRequestDTO requestDTO);

     List<ProductResponseDTO> getAllProducts();

     Page<ProductResponseDTO> getProductsPaged(Pageable pageable);

     Page<ProductResponseDTO> searchProducts(String keyword, Long categoryId, Long vendorId, BigDecimal minPrice, BigDecimal maxPrice, Boolean inStock, Pageable pageable);

     ProductResponseDTO getById(Long id);

     ProductResponseDTO getProductWithDetails(Long id);

     CompletableFuture<ProductResponseDTO> getProductAsync(Long id);

     List<ProductResponseDTO> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

     List<ProductResponseDTO> getLowStockProducts(Integer threshold);

     public ProductResponseDTO updateProductsById(Long id, ProductUpdateRequestDTO productUpdateRequestDTO);

     public ProductVariantResponseDTO createProductVariant(Long id, ProductVariantRequestDTO productVariantRequestDTO);

     public ProductVariantResponseDTO getProductVariantById(Long id);

     public List<ProductVariantResponseDTO> getAllProductVariantsById(Long id);

     public ProductVariantResponseDTO updateProductVariant(Long id, ProductVariantUpdateRequestDTO productVariantUpdateRequestDTO);

     public void deleteProductVariant(Long id);
}
