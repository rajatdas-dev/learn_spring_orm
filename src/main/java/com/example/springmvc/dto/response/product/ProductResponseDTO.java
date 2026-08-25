package com.example.springmvc.dto.response.product;

import com.example.springmvc.dto.request.product.ProductVariantRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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

    private List<ProductVariantResponseDTO> productVariants;

}
