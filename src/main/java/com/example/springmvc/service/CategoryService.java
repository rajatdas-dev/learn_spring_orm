package com.example.springmvc.service;

import com.example.springmvc.dto.request.CategoryRequestDTO;
import com.example.springmvc.dto.response.CategoryResponseDTO;

public interface CategoryService {

    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO);
}
