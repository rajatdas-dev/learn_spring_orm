package com.example.springmvc.controller;

import com.example.springmvc.dto.request.CategoryRequestDTO;
import com.example.springmvc.dto.response.CategoryResponseDTO;
import com.example.springmvc.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create-category")
    public ResponseEntity<CategoryResponseDTO> createResponse(@RequestBody CategoryRequestDTO categoryRequestDTO){

        return  ResponseEntity.ok(categoryService.createCategory(categoryRequestDTO));
    }
}
