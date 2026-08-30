package com.example.springmvc.dto.response.cart;

import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponseDTO {

    private Long id;
    private Long userId;
    @Builder.Default
    private List<CartItemResponseDTO> items = new ArrayList<>();
    private BigDecimal totalAmount;
    private Integer totalItems;
}
