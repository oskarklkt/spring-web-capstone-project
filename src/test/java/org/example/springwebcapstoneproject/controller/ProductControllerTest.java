package org.example.springwebcapstoneproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springwebcapstoneproject.dto.ProductDto;
import org.example.springwebcapstoneproject.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturnListOfProducts() throws Exception {
        // given
        List<ProductDto> mockProducts = List.of(
                new ProductDto(1L, "Nail gun", 8L, 23.95),
                new ProductDto(2L, "Hammer", 12L, 12.50)
        );

        when(productService.getAll()).thenReturn(mockProducts);

        // when + then
        mockMvc.perform(get("/api/v1/product"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockProducts)));

        verify(productService).getAll();
    }
}
