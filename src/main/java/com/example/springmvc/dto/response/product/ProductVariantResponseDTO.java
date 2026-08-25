package com.example.springmvc.dto.response.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantResponseDTO {

    private Long id;
    private String sku;
    private String color;
    private Integer size;
    private BigDecimal price;
}
