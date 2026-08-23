package com.example.springmvc.dto.response;

import com.example.springmvc.entity.Category;
import com.example.springmvc.entity.Product;
import com.example.springmvc.entity.Vendor;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {

    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private Long vendorId;
    private String vendorName;

    private Long categoryId;
    private String categoryName;

}
