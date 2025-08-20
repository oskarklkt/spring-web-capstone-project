package org.example.springwebcapstoneproject.service;

import org.example.springwebcapstoneproject.dto.cart.AddCartItemRequestDto;
import org.example.springwebcapstoneproject.dto.cart.CartDto;

public interface CartService {

    void addItem(AddCartItemRequestDto addCartItemRequestDto);
    CartDto get();
    void remove(Long productId);
    void changeQuantity(Long productId, Long newQuantity);
    void checkout();
}
