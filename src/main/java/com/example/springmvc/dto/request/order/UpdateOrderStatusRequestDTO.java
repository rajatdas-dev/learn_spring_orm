package com.example.springmvc.dto.request.order;

import com.example.springmvc.entity.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrderStatusRequestDTO {

    @NotNull(message = "New order status is required")
    private OrderStatus newStatus;

    private String remarks;
}
