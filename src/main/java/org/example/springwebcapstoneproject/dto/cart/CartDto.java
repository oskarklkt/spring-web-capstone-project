package org.example.springwebcapstoneproject.dto.cart;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private Map<Long, CartItemDto> cartItems;
    private Double subtotal;
}
