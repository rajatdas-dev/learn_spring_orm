package com.example.springmvc.controller;

import com.example.springmvc.dto.request.order.CreateOrderRequestDTO;
import com.example.springmvc.dto.request.order.UpdateOrderStatusRequestDTO;
import com.example.springmvc.dto.response.order.OrderResponseDTO;
import com.example.springmvc.response.ApiResponse;
import com.example.springmvc.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDTO>> createOrder(@Valid @RequestBody CreateOrderRequestDTO requestDTO) {
        OrderResponseDTO orderResponseDTO = orderService.createOrder(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order created successfully", orderResponseDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> getOrderById(@PathVariable Long id) {
        OrderResponseDTO orderResponseDTO = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponse.success("Order fetched successfully", orderResponseDTO));
    }

    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> getOrderByOrderNumber(@PathVariable String orderNumber) {
        OrderResponseDTO orderResponseDTO = orderService.getOrderByOrderNumber(orderNumber);
        return ResponseEntity.ok(ApiResponse.success("Order fetched successfully", orderResponseDTO));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderResponseDTO> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success("User orders fetched successfully", orders));
    }

    @GetMapping("/user/{userId}/page")
    public ResponseEntity<ApiResponse<Page<OrderResponseDTO>>> getOrdersByUserIdPaged(
            @PathVariable Long userId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<OrderResponseDTO> pagedOrders = orderService.getOrdersByUserId(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success("User orders page fetched successfully", pagedOrders));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequestDTO requestDTO) {
        OrderResponseDTO orderResponseDTO = orderService.updateOrderStatus(id, requestDTO);
        return ResponseEntity.ok(ApiResponse.success("Order status updated successfully", orderResponseDTO));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> cancelOrder(@PathVariable Long id) {
        OrderResponseDTO orderResponseDTO = orderService.cancelOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Order cancelled and inventory restored successfully", orderResponseDTO));
    }
}
