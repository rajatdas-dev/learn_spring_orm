package com.example.springmvc.dto.response.order;

import com.example.springmvc.entity.enums.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusHistoryResponseDTO {

    private Long id;
    private OrderStatus fromStatus;
    private OrderStatus toStatus;
    private String remarks;
    private LocalDateTime timestamp;
}
