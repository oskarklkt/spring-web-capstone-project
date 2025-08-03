package org.example.springwebcapstoneproject.mapper;

import org.example.springwebcapstoneproject.dto.cart.CartDto;
import org.example.springwebcapstoneproject.entity.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartDto toDto(Cart cart);
}
