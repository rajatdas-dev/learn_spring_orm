package com.example.springmvc.service.impl;

import com.example.springmvc.dto.request.cart.AddToCartRequestDTO;
import com.example.springmvc.dto.response.cart.CartItemResponseDTO;
import com.example.springmvc.dto.response.cart.CartResponseDTO;
import com.example.springmvc.entity.Cart;
import com.example.springmvc.entity.CartItem;
import com.example.springmvc.entity.ProductVariant;
import com.example.springmvc.entity.User;
import com.example.springmvc.exception.ErrorCode;
import com.example.springmvc.exception.InvalidRequestException;
import com.example.springmvc.exception.ResourceNotFoundException;
import com.example.springmvc.repository.CartItemRepository;
import com.example.springmvc.repository.CartRepository;
import com.example.springmvc.repository.ProductVariantRepository;
import com.example.springmvc.repository.UserRepository;
import com.example.springmvc.service.CartService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Override
    @Transactional
    public CartResponseDTO getCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return mapToResponse(cart);
    }

    @Override
    @Transactional
    public CartResponseDTO addToCart(Long userId, AddToCartRequestDTO requestDTO) {
        if (requestDTO.getQuantity() <= 0) {
            throw new InvalidRequestException(
                    ErrorCode.INVALID_REQUEST,
                    "Quantity must be greater than zero"
            );
        }

        Cart cart = getOrCreateCart(userId);
        ProductVariant variant = productVariantRepository.findById(requestDTO.getProductVariantId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.PRODUCT_VARIANT_NOT_FOUND,
                        "Product variant not found with id: " + requestDTO.getProductVariantId()
                ));

        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProductVariant().getId().equals(variant.getId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + requestDTO.getQuantity());
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .productVariant(variant)
                    .quantity(requestDTO.getQuantity())
                    .build();
            cart.addItem(newItem);
        }

        Cart savedCart = cartRepository.save(cart);
        return mapToResponse(savedCart);
    }

    @Override
    @Transactional
    public CartResponseDTO updateCartItem(Long userId, Long cartItemId, Integer quantity) {
        if (quantity <= 0) {
            return removeCartItem(userId, cartItemId);
        }

        Cart cart = getOrCreateCart(userId);
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.INVALID_REQUEST,
                        "Cart item not found with id: " + cartItemId
                ));

        item.setQuantity(quantity);
        Cart savedCart = cartRepository.save(cart);
        return mapToResponse(savedCart);
    }

    @Override
    @Transactional
    public CartResponseDTO removeCartItem(Long userId, Long cartItemId) {
        Cart cart = getOrCreateCart(userId);
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.INVALID_REQUEST,
                        "Cart item not found with id: " + cartItemId
                ));

        cart.removeItem(item);
        cartItemRepository.delete(item);
        Cart savedCart = cartRepository.save(cart);
        return mapToResponse(savedCart);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        cart.clear();
        cartRepository.save(cart);
    }

    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    ErrorCode.USER_NOT_FOUND,
                                    "User not found with id: " + userId
                            ));
                    Cart newCart = Cart.builder()
                            .user(user)
                            .items(new ArrayList<>())
                            .build();
                    return cartRepository.save(newCart);
                });
    }

    private CartResponseDTO mapToResponse(Cart cart) {
        List<CartItemResponseDTO> itemDTOs = cart.getItems().stream()
                .map(item -> {
                    ProductVariant variant = item.getProductVariant();
                    BigDecimal price = variant.getPrice();
                    BigDecimal subtotal = price.multiply(BigDecimal.valueOf(item.getQuantity()));
                    return CartItemResponseDTO.builder()
                            .id(item.getId())
                            .productVariantId(variant.getId())
                            .sku(variant.getSku())
                            .productName(variant.getProduct() != null ? variant.getProduct().getName() : "")
                            .color(variant.getColor())
                            .size(variant.getSize())
                            .unitPrice(price)
                            .quantity(item.getQuantity())
                            .subtotal(subtotal)
                            .build();
                })
                .toList();

        BigDecimal total = itemDTOs.stream()
                .map(CartItemResponseDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalCount = itemDTOs.stream()
                .mapToInt(CartItemResponseDTO::getQuantity)
                .sum();

        return CartResponseDTO.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .items(itemDTOs)
                .totalAmount(total)
                .totalItems(totalCount)
                .build();
    }
}
