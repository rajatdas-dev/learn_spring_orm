package com.example.springmvc.dto.response.order;

import com.example.springmvc.entity.enums.PaymentMethod;
import com.example.springmvc.entity.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {

    private Long id;
    private PaymentStatus status;
    private PaymentMethod method;
    private BigDecimal amount;
    private String transactionId;
    private LocalDateTime paymentDate;
}
