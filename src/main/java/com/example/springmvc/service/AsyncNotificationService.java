package com.example.springmvc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AsyncNotificationService {

    @Async("shopsphereTaskExecutor")
    public void sendOrderConfirmation(Long orderId) {
        log.info("[ASYNC] Sending order confirmation email for order ID: {}", orderId);
        try {
            // Simulate sending email / generating PDF invoice
            Thread.sleep(100);
            log.info("[ASYNC] Order confirmation email sent successfully for order ID: {}", orderId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[ASYNC] Email sending interrupted for order ID: {}", orderId);
        }
    }

    @Async("shopsphereTaskExecutor")
    public void generateInvoice(Long orderId) {
        log.info("[ASYNC] Generating invoice for order ID: {}", orderId);
        try {
            Thread.sleep(100);
            log.info("[ASYNC] Invoice generated successfully for order ID: {}", orderId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[ASYNC] Invoice generation interrupted for order ID: {}", orderId);
        }
    }

    @Async("shopsphereTaskExecutor")
    public void sendFailingNotification(Long orderId) {
        log.info("[ASYNC] Simulating uncaught async exception for order ID: {}", orderId);
        throw new RuntimeException("Simulated uncaught async exception for testing AsyncExceptionHandler (orderId: " + orderId + ")");
    }
}
