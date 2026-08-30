package com.example.springmvc.service.impl;

import com.example.springmvc.dto.request.order.CreateOrderRequestDTO;
import com.example.springmvc.dto.request.order.OrderItemRequestDTO;
import com.example.springmvc.dto.request.order.UpdateOrderStatusRequestDTO;
import com.example.springmvc.dto.response.order.*;
import com.example.springmvc.entity.*;
import com.example.springmvc.entity.enums.*;
import com.example.springmvc.exception.*;
import com.example.springmvc.repository.*;
import com.example.springmvc.service.AsyncNotificationService;
import com.example.springmvc.service.CartService;
import com.example.springmvc.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private StockTransactionRepository stockTransactionRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private AsyncNotificationService asyncNotificationService;

    @Override
    @Transactional
    public OrderResponseDTO createOrder(CreateOrderRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.USER_NOT_FOUND,
                        "User not found with id: " + requestDTO.getUserId()
                ));

        String orderNumber = "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        Order order = Order.builder()
                .orderNumber(orderNumber)
                .user(user)
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
                .orderItems(new ArrayList<>())
                .statusHistories(new ArrayList<>())
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequestDTO itemDTO : requestDTO.getItems()) {
            ProductVariant variant = productVariantRepository.findById(itemDTO.getProductVariantId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            ErrorCode.PRODUCT_VARIANT_NOT_FOUND,
                            "Product variant not found with id: " + itemDTO.getProductVariantId()
                    ));

            // Concurrency: Use pessimistic locking to safely verify and deduct inventory
            Inventory inventory = inventoryRepository.findByProductVariantIdWithLock(variant.getId())
                    .orElseThrow(() -> new ProductOutOfStockException(
                            "Inventory record not found for variant: " + variant.getSku()
                    ));

            if (inventory.getQuantity() < itemDTO.getQuantity()) {
                throw new ProductOutOfStockException(
                        "Only " + inventory.getQuantity() + " units available for SKU " + variant.getSku()
                );
            }

            // Deduct inventory
            inventory.setQuantity(inventory.getQuantity() - itemDTO.getQuantity());
            inventoryRepository.save(inventory);

            // Record stock audit trail
            StockTransaction stockTxn = StockTransaction.builder()
                    .productVariant(variant)
                    .type(StockTransactionType.OUTBOUND)
                    .quantity(itemDTO.getQuantity())
                    .balanceAfter(inventory.getQuantity())
                    .referenceId(orderNumber)
                    .build();
            stockTransactionRepository.save(stockTxn);

            BigDecimal unitPrice = variant.getPrice();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
            total = total.add(subtotal);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .productVariant(variant)
                    .quantity(itemDTO.getQuantity())
                    .unitPrice(unitPrice)
                    .subtotal(subtotal)
                    .build();

            order.addOrderItem(orderItem);
        }

        order.setTotalAmount(total);

        // Payment record
        Payment payment = Payment.builder()
                .order(order)
                .status(requestDTO.getPaymentMethod() == PaymentMethod.CASH_ON_DELIVERY ? PaymentStatus.PENDING : PaymentStatus.COMPLETED)
                .method(requestDTO.getPaymentMethod())
                .amount(total)
                .transactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .build();
        order.setPayment(payment);

        // Shipment record
        Shipment shipment = Shipment.builder()
                .order(order)
                .status(ShipmentStatus.PREPARING)
                .carrier("ShopSphere Express")
                .trackingNumber("TRK-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase())
                .build();
        order.setShipment(shipment);

        // Order Status History
        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(order)
                .fromStatus(null)
                .toStatus(OrderStatus.PENDING)
                .remarks("Order placed successfully")
                .build();
        order.addStatusHistory(history);

        Order savedOrder = orderRepository.save(order);

        // Clear user's cart if any
        try {
            cartService.clearCart(user.getId());
        } catch (Exception ignored) {
            // Cart might not exist or empty
        }

        // Trigger Asynchronous tasks (order confirmation & invoice generation)
        asyncNotificationService.sendOrderConfirmation(savedOrder.getId());
        asyncNotificationService.generateInvoice(savedOrder.getId());

        return mapToResponse(savedOrder);
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.ORDER_NOT_FOUND,
                        "Order not found with id: " + id
                ));
        return mapToResponse(order);
    }

    @Override
    public OrderResponseDTO getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.ORDER_NOT_FOUND,
                        "Order not found with order number: " + orderNumber
                ));
        return mapToResponse(order);
    }

    @Override
    public List<OrderResponseDTO> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public Page<OrderResponseDTO> getOrdersByUserId(Long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional
    public OrderResponseDTO updateOrderStatus(Long orderId, UpdateOrderStatusRequestDTO requestDTO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.ORDER_NOT_FOUND,
                        "Order not found with id: " + orderId
                ));

        validateStateTransition(order.getStatus(), requestDTO.getNewStatus());

        OrderStatus previousStatus = order.getStatus();
        order.setStatus(requestDTO.getNewStatus());

        if (requestDTO.getNewStatus() == OrderStatus.SHIPPED && order.getShipment() != null) {
            order.getShipment().setStatus(ShipmentStatus.SHIPPED);
            order.getShipment().setShippedAt(LocalDateTime.now());
        } else if (requestDTO.getNewStatus() == OrderStatus.DELIVERED && order.getShipment() != null) {
            order.getShipment().setStatus(ShipmentStatus.DELIVERED);
            order.getShipment().setDeliveredAt(LocalDateTime.now());
            if (order.getPayment() != null) {
                order.getPayment().setStatus(PaymentStatus.COMPLETED);
            }
        }

        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(order)
                .fromStatus(previousStatus)
                .toStatus(requestDTO.getNewStatus())
                .remarks(requestDTO.getRemarks() != null ? requestDTO.getRemarks() : "Order status updated to " + requestDTO.getNewStatus())
                .build();
        order.addStatusHistory(history);

        Order savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponseDTO cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.ORDER_NOT_FOUND,
                        "Order not found with id: " + orderId
                ));

        validateStateTransition(order.getStatus(), OrderStatus.CANCELLED);

        // Restore inventory on cancellation
        for (OrderItem item : order.getOrderItems()) {
            Inventory inventory = inventoryRepository.findByProductVariantIdWithLock(item.getProductVariant().getId())
                    .orElse(null);
            if (inventory != null) {
                inventory.setQuantity(inventory.getQuantity() + item.getQuantity());
                inventoryRepository.save(inventory);

                StockTransaction stockTxn = StockTransaction.builder()
                        .productVariant(item.getProductVariant())
                        .type(StockTransactionType.RELEASED)
                        .quantity(item.getQuantity())
                        .balanceAfter(inventory.getQuantity())
                        .referenceId("CANCEL-" + order.getOrderNumber())
                        .build();
                stockTransactionRepository.save(stockTxn);
            }
        }

        OrderStatus previousStatus = order.getStatus();
        order.setStatus(OrderStatus.CANCELLED);

        if (order.getPayment() != null && order.getPayment().getStatus() == PaymentStatus.COMPLETED) {
            order.getPayment().setStatus(PaymentStatus.REFUNDED);
        }
        if (order.getShipment() != null) {
            order.getShipment().setStatus(ShipmentStatus.CANCELLED);
        }

        OrderStatusHistory history = OrderStatusHistory.builder()
                .order(order)
                .fromStatus(previousStatus)
                .toStatus(OrderStatus.CANCELLED)
                .remarks("Order was cancelled and stock was restored")
                .build();
        order.addStatusHistory(history);

        Order savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder);
    }

    private void validateStateTransition(OrderStatus current, OrderStatus next) {
        if (current == OrderStatus.DELIVERED) {
            throw new OrderStateException("Delivered order cannot be changed or cancelled");
        }
        if (current == OrderStatus.CANCELLED) {
            throw new OrderStateException("Cancelled order cannot be changed to another state");
        }
        if (current == OrderStatus.SHIPPED && next == OrderStatus.CANCELLED) {
            throw new OrderStateException("Shipped order cannot be cancelled directly");
        }
    }

    private OrderResponseDTO mapToResponse(Order order) {
        List<OrderItemResponseDTO> itemDTOs = order.getOrderItems().stream()
                .map(item -> OrderItemResponseDTO.builder()
                        .id(item.getId())
                        .productVariantId(item.getProductVariant().getId())
                        .sku(item.getProductVariant().getSku())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .subtotal(item.getSubtotal())
                        .build())
                .toList();

        PaymentResponseDTO paymentDTO = null;
        if (order.getPayment() != null) {
            Payment p = order.getPayment();
            paymentDTO = PaymentResponseDTO.builder()
                    .id(p.getId())
                    .status(p.getStatus())
                    .method(p.getMethod())
                    .amount(p.getAmount())
                    .transactionId(p.getTransactionId())
                    .paymentDate(p.getPaymentDate())
                    .build();
        }

        ShipmentResponseDTO shipmentDTO = null;
        if (order.getShipment() != null) {
            Shipment s = order.getShipment();
            shipmentDTO = ShipmentResponseDTO.builder()
                    .id(s.getId())
                    .status(s.getStatus())
                    .trackingNumber(s.getTrackingNumber())
                    .carrier(s.getCarrier())
                    .shippedAt(s.getShippedAt())
                    .deliveredAt(s.getDeliveredAt())
                    .build();
        }

        List<OrderStatusHistoryResponseDTO> historyDTOs = order.getStatusHistories().stream()
                .map(h -> OrderStatusHistoryResponseDTO.builder()
                        .id(h.getId())
                        .fromStatus(h.getFromStatus())
                        .toStatus(h.getToStatus())
                        .remarks(h.getRemarks())
                        .timestamp(h.getTimestamp())
                        .build())
                .toList();

        return OrderResponseDTO.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(order.getUser() != null ? order.getUser().getId() : null)
                .userName(order.getUser() != null ? order.getUser().getName() : null)
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .items(itemDTOs)
                .payment(paymentDTO)
                .shipment(shipmentDTO)
                .statusHistory(historyDTOs)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}
