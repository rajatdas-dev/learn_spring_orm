package com.example.springmvc.service.impl;

import com.example.springmvc.dto.request.ProductRequestDTO;
import com.example.springmvc.dto.response.ProductResponseDTO;
import com.example.springmvc.repository.ProductRepository;
import com.example.springmvc.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        return productRepository.save(requestDTO);
    }
}
