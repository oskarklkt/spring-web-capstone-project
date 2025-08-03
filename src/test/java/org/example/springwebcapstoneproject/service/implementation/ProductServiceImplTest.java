package org.example.springwebcapstoneproject.service.implementation;

import org.example.springwebcapstoneproject.dto.ProductDto;
import org.example.springwebcapstoneproject.entity.Product;
import org.example.springwebcapstoneproject.mapper.ProductMapper;
import org.example.springwebcapstoneproject.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAllProductsAsDtoList() {
        // given
        Product product1 = Product.builder().id(1L).title("Hammer").available(5L).price(10.99).build();
        Product product2 = Product.builder().id(2L).title("Drill").available(3L).price(79.99).build();

        ProductDto dto1 = new ProductDto(1L, "Hammer", 5L, 10.99);
        ProductDto dto2 = new ProductDto(2L, "Drill", 3L, 79.99);

        when(productRepository.findAll()).thenReturn(List.of(product1, product2));
        when(productMapper.toDto(product1)).thenReturn(dto1);
        when(productMapper.toDto(product2)).thenReturn(dto2);

        // when
        List<ProductDto> result = productService.getAll();

        // then
        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
        verify(productRepository).findAll();
        verify(productMapper).toDto(product1);
        verify(productMapper).toDto(product2);
    }
}
