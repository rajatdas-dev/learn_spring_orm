package com.example.springmvc.service;

import com.example.springmvc.dto.request.ProductRequestDTO;
import com.example.springmvc.dto.response.ProductResponseDTO;

import java.util.List;

public interface ProductService {
     ProductResponseDTO createProduct(ProductRequestDTO requestDTO);

     List<ProductResponseDTO> getAllProducts();

     ProductResponseDTO getById(Long id);
}
