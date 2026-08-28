package com.example.springmvc.dto.request.inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRequestDTO {

    @NotNull(message = "Quantity is required")
    @Min(value = 0,message = "Quantity cannot be negative")
    private Integer quantity;

    private Integer reservedQuantity;
}
