package com.example.springmvc.dto.request.category;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRequestDTO {

    @NotBlank(message = "name is required")
    private String name;

    private String description;

    @Builder.Default
    private boolean active = true;
}
