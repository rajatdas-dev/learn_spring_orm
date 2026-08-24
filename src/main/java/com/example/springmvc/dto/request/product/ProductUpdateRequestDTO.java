package com.example.springmvc.dto.request.product;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductUpdateRequestDTO {

    private String name;
    private BigDecimal price;
    private Integer stock;
}
