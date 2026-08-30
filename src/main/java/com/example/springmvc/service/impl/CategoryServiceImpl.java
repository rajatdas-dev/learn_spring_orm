package com.example.springmvc.service.impl;

import com.example.springmvc.dto.request.category.CategoryRequestDTO;
import com.example.springmvc.dto.response.category.CategoryResponseDTO;
import com.example.springmvc.entity.Category;
import com.example.springmvc.exception.ErrorCode;
import com.example.springmvc.exception.ResourceNotFoundException;
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

        Category category = new Category();

        category.setName(categoryRequestDTO.getName());
        category.setDescription(categoryRequestDTO.getDescription());

        Category savedCategory =  categoryRepository.save(category);

        return modelMapperUtil.map(savedCategory, CategoryResponseDTO.class);
    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {

        List<Category> categoryList = categoryRepository.findAll();

        return categoryList.stream()
                .map(category -> modelMapperUtil.map(category, CategoryResponseDTO.class))
                .toList();
    }

    @Override
    public CategoryResponseDTO findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(
                        ErrorCode.CATEGORY_NOT_FOUND,
                        " Category Not Found"));

        return modelMapperUtil.map(category, CategoryResponseDTO.class);
    }

    @Transactional
    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(
                        ErrorCode.CATEGORY_NOT_FOUND,
                        "Category Not Found"
                ));

        category.setName(requestDTO.getName());
        category.setDescription(requestDTO.getDescription());

        return modelMapperUtil.map(category, CategoryResponseDTO.class);
    }

    @Transactional
    @Override
    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(
                        ErrorCode.CATEGORY_NOT_FOUND,
                        "Category Not Found !!"
                ));

        categoryRepository.delete(category);
    }

    @Transactional
    @Override
    public List<CategoryResponseDTO> getChildCategories(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(
                        ErrorCode.CATEGORY_NOT_FOUND,
                        "Category Not found !!"
                ));

        List<Category> categoryChildList = category.getChildren();

        List<CategoryResponseDTO> categoryResponseDTOS = categoryChildList.stream()
                .map((childCategory)-> modelMapperUtil.map(childCategory, CategoryResponseDTO.class))
                .toList();

        return categoryResponseDTOS;
    }


}
