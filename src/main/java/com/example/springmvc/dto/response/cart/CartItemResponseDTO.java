package com.example.springmvc.dto.response.cart;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponseDTO {

    private Long id;
    private Long productVariantId;
    private String sku;
    private String productName;
    private String color;
    private Integer size;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal subtotal;
}
