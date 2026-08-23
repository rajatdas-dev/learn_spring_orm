package com.example.springmvc.service;

import com.example.springmvc.dto.request.CategoryRequestDTO;
import com.example.springmvc.dto.response.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {

    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO);

    public List<CategoryResponseDTO> getAllCategories();

    public CategoryResponseDTO findById(Long id);
}
