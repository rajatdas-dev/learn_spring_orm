package com.example.springmvc.dto.request.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantRequestDTO {

    private String sku;
    private String color;
    private Integer size;
    private BigDecimal price;
}
