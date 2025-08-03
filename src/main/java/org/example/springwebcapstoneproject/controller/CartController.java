package org.example.springwebcapstoneproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.springwebcapstoneproject.dto.cart.AddCartItemRequestDto;
import org.example.springwebcapstoneproject.dto.cart.CartDto;
import org.example.springwebcapstoneproject.exception.UserNotLoggedInException;
import org.example.springwebcapstoneproject.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CartController {

    private final CartService cartService;

    private void ensureUserLoggedIn(HttpServletRequest request) {
        if (request.getSession(false) == null || request.getSession().getAttribute("userId") == null) {
            throw new UserNotLoggedInException();
        }
    }

    @PostMapping("/v1/cart/add")
    public ResponseEntity<Void> addItem(@RequestBody AddCartItemRequestDto addCartItemRequestDto,
                                        HttpServletRequest request) {
        ensureUserLoggedIn(request);
        cartService.addItem(addCartItemRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/v1/cart")
    public ResponseEntity<CartDto> getCart(HttpServletRequest request) {
        ensureUserLoggedIn(request);
        return ResponseEntity.ok(cartService.get());
    }

    @DeleteMapping("/v1/cart/remove/{productId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long productId,
                                           HttpServletRequest request) {
        ensureUserLoggedIn(request);
        cartService.remove(productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/v1/cart/checkout")
    public ResponseEntity<String> checkout(HttpServletRequest request) {
        ensureUserLoggedIn(request);
        cartService.checkout();
        return ResponseEntity.ok("Order confirmed!");
    }

    @PostMapping("/v1/cart")
    public ResponseEntity<Void> changeQuantity(@RequestBody AddCartItemRequestDto addCartItemRequestDto,
                                               HttpServletRequest request) {
        ensureUserLoggedIn(request);
        cartService.changeQuantity(addCartItemRequestDto.getProductId(), addCartItemRequestDto.getQuantity());
        return ResponseEntity.ok().build();
    }
}
