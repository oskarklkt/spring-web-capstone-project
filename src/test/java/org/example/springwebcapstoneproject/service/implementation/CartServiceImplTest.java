package org.example.springwebcapstoneproject.service.implementation;

import org.example.springwebcapstoneproject.dto.cart.AddCartItemRequestDto;
import org.example.springwebcapstoneproject.dto.cart.CartItemDto;
import org.example.springwebcapstoneproject.entity.Cart;
import org.example.springwebcapstoneproject.entity.Product;
import org.example.springwebcapstoneproject.exception.NotEnoughProductsAvailableException;
import org.example.springwebcapstoneproject.exception.ProductNotExistsException;
import org.example.springwebcapstoneproject.mapper.CartMapper;
import org.example.springwebcapstoneproject.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    private ProductRepository productRepository;
    private Cart cart;
    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        cart = mock(Cart.class);
        CartMapper cartMapper = mock(CartMapper.class);
        cartService = new CartServiceImpl(productRepository, cart, cartMapper);
    }

    @Test
    void shouldAddItemToCartSuccessfully() {
        AddCartItemRequestDto dto = new AddCartItemRequestDto(1L, 2L);
        Product product = new Product(1L, "Drill", 10L, 49.99);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        cartService.addItem(dto);

        assertEquals(8L, product.getAvailable());
        verify(productRepository).save(product);
        verify(cart).addItem(eq(1L), any(CartItemDto.class));
    }

    @Test
    void shouldThrowWhenProductNotFoundOnAdd() {
        AddCartItemRequestDto dto = new AddCartItemRequestDto(1L, 2L);
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotExistsException.class, () -> cartService.addItem(dto));
    }

    @Test
    void shouldThrowWhenNotEnoughAvailableOnAdd() {
        AddCartItemRequestDto dto = new AddCartItemRequestDto(1L, 20L);
        Product product = new Product(1L, "Saw", 5L, 29.99);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(NotEnoughProductsAvailableException.class, () -> cartService.addItem(dto));
    }

    @Test
    void shouldRemoveItemSuccessfully() {
        Product product = new Product(1L, "Hammer", 2L, 19.99);
        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cart.getProductQuantity(1L)).thenReturn(1L);

        cartService.remove(1L);

        assertEquals(3L, product.getAvailable());
        verify(cart).removeItem(1L);
        verify(productRepository).save(product);
    }

    @Test
    void shouldThrowWhenRemovingNonExistingProduct() {
        when(productRepository.existsById(1L)).thenReturn(false);
        assertThrows(ProductNotExistsException.class, () -> cartService.remove(1L));
    }

    @Test
    void shouldChangeQuantitySuccessfully() {
        Product product = new Product(1L, "Wrench", 5L, 9.99);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cart.getProductQuantity(1L)).thenReturn(2L);

        cartService.changeQuantity(1L, 4L);

        assertEquals(3L, product.getAvailable());
        verify(productRepository).save(product);
        verify(cart).changeQuantity(1L, 4L);
    }

    @Test
    void shouldThrowWhenChangingQuantityAndProductMissing() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotExistsException.class, () -> cartService.changeQuantity(1L, 5L));
    }

    @Test
    void shouldCheckoutSuccessfully() {
        Product product = new Product(1L, "Paint", 10L, 14.99);
        CartItemDto itemDto = new CartItemDto(1L, "Paint", 2L, 14.99);
        Map<Long, CartItemDto> items = new HashMap<>();
        items.put(1L, itemDto);

        when(cart.getCartItems()).thenReturn(items);
        when(productRepository.findByTitle("Paint")).thenReturn(Optional.of(product));

        cartService.checkout();

        assertEquals(8L, product.getAvailable());
        verify(productRepository).save(product);
        verify(cart).clear();
    }

    @Test
    void shouldThrowOnCheckoutWhenQuantityInsufficient() {
        Product product = new Product(1L, "Brush", 1L, 9.99);
        CartItemDto itemDto = new CartItemDto(1L, "Brush", 3L, 9.99);
        Map<Long, CartItemDto> items = new HashMap<>();
        items.put(1L, itemDto);

        when(cart.getCartItems()).thenReturn(items);
        when(productRepository.findByTitle("Brush")).thenReturn(Optional.of(product));

        assertThrows(NotEnoughProductsAvailableException.class, () -> cartService.checkout());
    }
}
