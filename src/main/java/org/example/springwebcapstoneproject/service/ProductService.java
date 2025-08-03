package org.example.springwebcapstoneproject.service;

import org.example.springwebcapstoneproject.dto.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAll();
}
