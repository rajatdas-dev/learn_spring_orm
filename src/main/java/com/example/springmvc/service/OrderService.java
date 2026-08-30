package com.example.springmvc.service;

import com.example.springmvc.dto.request.order.CreateOrderRequestDTO;
import com.example.springmvc.dto.request.order.UpdateOrderStatusRequestDTO;
import com.example.springmvc.dto.response.order.OrderResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    OrderResponseDTO createOrder(CreateOrderRequestDTO requestDTO);

    OrderResponseDTO getOrderById(Long id);

    OrderResponseDTO getOrderByOrderNumber(String orderNumber);

    List<OrderResponseDTO> getOrdersByUserId(Long userId);

    Page<OrderResponseDTO> getOrdersByUserId(Long userId, Pageable pageable);

    OrderResponseDTO updateOrderStatus(Long orderId, UpdateOrderStatusRequestDTO requestDTO);

    OrderResponseDTO cancelOrder(Long orderId);
}
