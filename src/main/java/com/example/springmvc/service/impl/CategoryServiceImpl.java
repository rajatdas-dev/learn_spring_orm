package com.example.springmvc.service.impl;

import com.example.springmvc.dto.request.CategoryRequestDTO;
import com.example.springmvc.dto.response.CategoryResponseDTO;
import com.example.springmvc.entity.Category;
import com.example.springmvc.repository.CategoryRepository;
import com.example.springmvc.service.CategoryService;
import com.example.springmvc.util.mapper.ModelMapperUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapperUtil modelMapperUtil;
//    private final ProductMapper productMapper;

    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {

        Category category = toEntity(categoryRequestDTO);

        categoryRepository.save(category);

        return  toResponseDTO(category);
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {

        List<Category> categoryList = categoryRepository.findAll();

        return categoryList.stream()
                .map(category -> modelMapperUtil.map(category, CategoryResponseDTO.class))
                .toList();
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
