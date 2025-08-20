package org.example.springwebcapstoneproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.springwebcapstoneproject.dto.cart.AddCartItemRequestDto;
import org.example.springwebcapstoneproject.dto.cart.CartDto;
import org.example.springwebcapstoneproject.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<Void> addItem(@RequestBody AddCartItemRequestDto addCartItemRequestDto) {
        cartService.addItem(addCartItemRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<CartDto> getCart() {
        return ResponseEntity.ok(cartService.get());
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long productId) {
        cartService.remove(productId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/items") public ResponseEntity<Void> changeQuantity(
            @RequestBody AddCartItemRequestDto addCartItemRequestDto) {
        cartService.changeQuantity(addCartItemRequestDto.getProductId(), addCartItemRequestDto.getQuantity());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout() {
        cartService.checkout();
        return ResponseEntity.ok("Order confirmed!");
    }
}
