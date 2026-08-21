package com.example.springmvc.dto.request;

import com.example.springmvc.entity.Category;
import com.example.springmvc.entity.Vendor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {

    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private Vendor vendor;
    private Category category;
}
