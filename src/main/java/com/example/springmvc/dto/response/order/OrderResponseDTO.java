package com.example.springmvc.dto.response.order;

import com.example.springmvc.entity.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    private Long id;
    private String orderNumber;
    private Long userId;
    private String userName;
    private OrderStatus status;
    private BigDecimal totalAmount;
    @Builder.Default
    private List<OrderItemResponseDTO> items = new ArrayList<>();
    private PaymentResponseDTO payment;
    private ShipmentResponseDTO shipment;
    @Builder.Default
    private List<OrderStatusHistoryResponseDTO> statusHistory = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
