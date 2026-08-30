package com.example.springmvc.service;

import com.example.springmvc.dto.request.cart.AddToCartRequestDTO;
import com.example.springmvc.dto.response.cart.CartResponseDTO;

public interface CartService {

    CartResponseDTO getCart(Long userId);

    CartResponseDTO addToCart(Long userId, AddToCartRequestDTO requestDTO);

    CartResponseDTO updateCartItem(Long userId, Long cartItemId, Integer quantity);

    CartResponseDTO removeCartItem(Long userId, Long cartItemId);

    void clearCart(Long userId);
}
