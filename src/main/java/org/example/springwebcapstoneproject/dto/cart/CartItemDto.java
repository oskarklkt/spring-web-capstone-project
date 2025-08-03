package org.example.springwebcapstoneproject.dto.cart;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {
    private Long ordinal;
    private String name;
    private Long quantity;
    private Double price;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof CartItemDto that)) return false;

        return name.equals(that.name) && price.equals(that.price);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + price.hashCode();
        return result;
    }
}
