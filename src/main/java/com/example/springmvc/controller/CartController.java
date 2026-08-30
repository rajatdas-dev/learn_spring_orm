package com.example.springmvc.controller;

import com.example.springmvc.dto.request.cart.AddToCartRequestDTO;
import com.example.springmvc.dto.response.cart.CartResponseDTO;
import com.example.springmvc.response.ApiResponse;
import com.example.springmvc.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<CartResponseDTO>> getCart(@PathVariable Long userId) {
        CartResponseDTO cartResponseDTO = cartService.getCart(userId);
        return ResponseEntity.ok(ApiResponse.success("Cart fetched successfully", cartResponseDTO));
    }

    @PostMapping("/user/{userId}/items")
    public ResponseEntity<ApiResponse<CartResponseDTO>> addToCart(
            @PathVariable Long userId,
            @Valid @RequestBody AddToCartRequestDTO requestDTO) {
        CartResponseDTO cartResponseDTO = cartService.addToCart(userId, requestDTO);
        return ResponseEntity.ok(ApiResponse.success("Item added to cart successfully", cartResponseDTO));
    }

    @PutMapping("/user/{userId}/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartResponseDTO>> updateCartItem(
            @PathVariable Long userId,
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity) {
        CartResponseDTO cartResponseDTO = cartService.updateCartItem(userId, cartItemId, quantity);
        return ResponseEntity.ok(ApiResponse.success("Cart item updated successfully", cartResponseDTO));
    }

    @DeleteMapping("/user/{userId}/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartResponseDTO>> removeCartItem(
            @PathVariable Long userId,
            @PathVariable Long cartItemId) {
        CartResponseDTO cartResponseDTO = cartService.removeCartItem(userId, cartItemId);
        return ResponseEntity.ok(ApiResponse.success("Cart item removed successfully", cartResponseDTO));
    }

    @DeleteMapping("/user/{userId}/clear")
    public ResponseEntity<ApiResponse<Void>> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok(ApiResponse.success("Cart cleared successfully"));
    }
}
