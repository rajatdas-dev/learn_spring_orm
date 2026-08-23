package com.example.springmvc.controller;

import com.example.springmvc.dto.request.CategoryRequestDTO;
import com.example.springmvc.dto.response.CategoryResponseDTO;
import com.example.springmvc.response.ApiResponse;
import com.example.springmvc.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create-category")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> createResponse(@RequestBody CategoryRequestDTO categoryRequestDTO){

        CategoryResponseDTO categoryResponseDTO = categoryService.createCategory(categoryRequestDTO);

        return ResponseEntity.ok(
                ApiResponse.success("Category Created SuccessFully",categoryResponseDTO)
        );
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<CategoryResponseDTO>>> getAllCategories(){

        List<CategoryResponseDTO> categoryResponseDTOList = categoryService.getAllCategories();

        return ResponseEntity.ok()
                .body(
                        ApiResponse.success(
                                "All Categories successfully fetched",
                                categoryResponseDTOList
                        )
                );
    }
}
