package com.example.springmvc.service;

import com.example.springmvc.dto.request.ProductRequestDTO;
import com.example.springmvc.dto.response.ProductResponseDTO;

public interface ProductService {
     ProductResponseDTO createProduct(ProductRequestDTO requestDTO);
}
