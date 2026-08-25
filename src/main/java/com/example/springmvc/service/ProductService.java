package com.example.springmvc.service;

import com.example.springmvc.dto.request.product.ProductRequestDTO;
import com.example.springmvc.dto.request.product.ProductUpdateRequestDTO;
import com.example.springmvc.dto.request.product.ProductVariantRequestDTO;
import com.example.springmvc.dto.request.product.ProductVariantUpdateRequestDTO;
import com.example.springmvc.dto.response.product.ProductResponseDTO;
import com.example.springmvc.dto.response.product.ProductVariantResponseDTO;

import java.util.List;

public interface ProductService {
     ProductResponseDTO createProduct(ProductRequestDTO requestDTO);

     List<ProductResponseDTO> getAllProducts();

     ProductResponseDTO getById(Long id);

     public ProductResponseDTO updateProductsById(Long id, ProductUpdateRequestDTO productUpdateRequestDTO);

     public ProductVariantResponseDTO createProductVariant(Long id, ProductVariantRequestDTO productVariantRequestDTO);

     public ProductVariantResponseDTO getProductVariantById(Long id);

     public List<ProductVariantResponseDTO> getAllProductVariantsById(Long id);

     public ProductVariantResponseDTO updateProductVariant(Long id, ProductVariantUpdateRequestDTO productVariantUpdateRequestDTO);

     public void deleteProductVariant(Long id);
}
