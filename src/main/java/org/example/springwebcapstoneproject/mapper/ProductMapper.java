package org.example.springwebcapstoneproject.mapper;

import org.example.springwebcapstoneproject.dto.ProductDto;
import org.example.springwebcapstoneproject.entity.Product;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ProductMapper {

    Product toEntity(ProductDto dto);
    ProductDto toDto(Product dto);
}
