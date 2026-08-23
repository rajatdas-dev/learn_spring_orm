package com.example.springmvc.service.impl;

import com.example.springmvc.dto.request.CategoryRequestDTO;
import com.example.springmvc.dto.response.CategoryResponseDTO;
import com.example.springmvc.entity.Category;
import com.example.springmvc.repository.CategoryRepository;
import com.example.springmvc.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
//    private final ProductMapper productMapper;

    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {

        Category category = toEntity(categoryRequestDTO);

        categoryRepository.save(category);

        return  toResponseDTO(category);
    }

    CategoryResponseDTO toResponseDTO(Category category){

        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();

        categoryResponseDTO.setId(category.getId());
        categoryResponseDTO.setName(category.getName());

        return  categoryResponseDTO;
    }

    Category toEntity(CategoryRequestDTO categoryRequestDTO){

        Category category = new Category();
        category.setName(categoryRequestDTO.getName());

        return  category;
    }
}
