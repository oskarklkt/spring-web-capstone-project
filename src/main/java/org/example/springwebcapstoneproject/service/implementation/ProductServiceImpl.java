package org.example.springwebcapstoneproject.service.implementation;

import lombok.RequiredArgsConstructor;
import org.example.springwebcapstoneproject.dto.ProductDto;
import org.example.springwebcapstoneproject.mapper.ProductMapper;
import org.example.springwebcapstoneproject.repository.ProductRepository;
import org.example.springwebcapstoneproject.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @Override
    public List<ProductDto> getAll() {
        return productRepository.findAll().stream().map(productMapper::toDto).toList();
    }
}
