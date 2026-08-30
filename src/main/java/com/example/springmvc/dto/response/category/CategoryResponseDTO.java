package com.example.springmvc.dto.response.category;

import com.example.springmvc.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDTO {

    private Long id;
    private String name;
    private String description;
    private String slug;
    private boolean active;
    private CategoryResponseDTO parent;
    private List<CategoryResponseDTO> children;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
