package com.example.springmvc.dto.request.product;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantUpdateRequestDTO {

    private String sku;

    private String color;

    @NotBlank
    private Integer size;

    @NotBlank
    private BigDecimal price;
}
