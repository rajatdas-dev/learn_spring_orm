package com.example.springmvc.dto.request.inventory;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRequestDTO {

    @NotBlank
    private Integer quantity;

    private Integer reservedQuantity;
}
