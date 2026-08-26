package com.example.springmvc.dto.response.inventory;

import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponseDTO {

    private Long id;
    private Integer quantity;
    private Integer reservedQuantity;
    @Version
    private Integer version;
}
