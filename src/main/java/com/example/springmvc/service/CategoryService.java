package com.example.springmvc.service;

import com.example.springmvc.dto.request.category.CategoryRequestDTO;
import com.example.springmvc.dto.response.category.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {

    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO);

    public List<CategoryResponseDTO> getAllCategories();

    public CategoryResponseDTO findById(Long id);

    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO);

    public void deleteCategory(Long id);

    public List<CategoryResponseDTO> getChildCategories(Long id);
}

