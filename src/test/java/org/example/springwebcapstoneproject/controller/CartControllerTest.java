package org.example.springwebcapstoneproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springwebcapstoneproject.dto.cart.AddCartItemRequestDto;
import org.example.springwebcapstoneproject.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc(addFilters = false)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    private AddCartItemRequestDto sampleItem;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        sampleItem = new AddCartItemRequestDto(1L, 3L);
        session = new MockHttpSession();
        session.setAttribute("userId", 123L); // simulate logged-in user
    }

    @Test
    void addItem_shouldReturnOk() throws Exception {
        mockMvc.perform(post("/api/v1/cart/add")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleItem)))
                .andExpect(status().isOk());

        verify(cartService, times(1)).addItem(any(AddCartItemRequestDto.class));
    }

    @Test
    void removeItem_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/api/v1/cart/remove/{productId}", 1L)
                        .session(session))
                .andExpect(status().isOk());

        verify(cartService, times(1)).remove(1L);
    }

    @Test
    void checkout_shouldReturnConfirmation() throws Exception {
        mockMvc.perform(post("/api/v1/cart/checkout")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string("Order confirmed!"));

        verify(cartService, times(1)).checkout();
    }

    @Test
    void changeQuantity_shouldReturnOk() throws Exception {
        mockMvc.perform(post("/api/v1/cart")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleItem)))
                .andExpect(status().isOk());

        verify(cartService, times(1)).changeQuantity(1L, 3L);
    }
}
