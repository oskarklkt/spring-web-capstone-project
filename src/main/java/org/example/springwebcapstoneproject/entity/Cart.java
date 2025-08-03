package org.example.springwebcapstoneproject.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.springwebcapstoneproject.dto.cart.CartItemDto;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
@SessionScope
@RequiredArgsConstructor
public class Cart {

    private final Map<Long, CartItemDto> cartItems = new HashMap<>();
    private Double subtotal = 0d;

    public void addItem(Long productId, CartItemDto cartItemDto) {
        if (cartItems.containsKey(productId)) {
            Long newQuantity = cartItems.get(productId).getQuantity() + cartItemDto.getQuantity();
            cartItemDto.setQuantity(newQuantity);
        }
        cartItemDto.setOrdinal(cartItems.size() + 1L);
        cartItems.put(productId, cartItemDto);
    }

    public void removeItem(Long productId) {
        cartItems.remove(productId);
    }

    public void changeQuantity(Long productId, Long quantity) {
        cartItems.get(productId).setQuantity(quantity);
    }

    public Long getProductQuantity(Long productId) {
        CartItemDto cartItemDto = cartItems.get(productId);
        if (cartItemDto == null) {
            return 0L;
        }
        return cartItemDto.getQuantity();
    }

    public void clear() {
        cartItems.clear();
    }

    public void updateSubtotal() {
        subtotal = cartItems.values().stream()
                .mapToDouble(item -> (item.getQuantity() * item.getPrice()))
                .sum();
    }

}
