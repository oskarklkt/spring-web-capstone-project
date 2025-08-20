package org.example.springwebcapstoneproject.service.implementation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.springwebcapstoneproject.dto.cart.AddCartItemRequestDto;
import org.example.springwebcapstoneproject.dto.cart.CartDto;
import org.example.springwebcapstoneproject.dto.cart.CartItemDto;
import org.example.springwebcapstoneproject.model.Cart;
import org.example.springwebcapstoneproject.entity.Product;
import org.example.springwebcapstoneproject.exception.NotEnoughProductsAvailableException;
import org.example.springwebcapstoneproject.exception.ProductNotExistsException;
import org.example.springwebcapstoneproject.mapper.CartMapper;
import org.example.springwebcapstoneproject.repository.ProductRepository;
import org.example.springwebcapstoneproject.service.CartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final ProductRepository productRepository;
    private final Cart cart;
    private final CartMapper cartMapper;

    @Override
    public CartDto get() {
        cart.updateSubtotal();
        return cartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public void addItem(AddCartItemRequestDto addCartItemRequestDto) {
        Long productId = addCartItemRequestDto.getProductId();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotExistsException(productId));

        if (product.getAvailable() < addCartItemRequestDto.getQuantity()) {
            throw new NotEnoughProductsAvailableException(productId);
        }

        product.setAvailable(product.getAvailable() - addCartItemRequestDto.getQuantity());
        productRepository.save(product);

        cart.addItem(productId, CartItemDto.builder()
                .name(product.getTitle())
                .price(product.getPrice())
                .quantity(addCartItemRequestDto.getQuantity())
                .build());
    }

    @Override
    @Transactional
    public void remove(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotExistsException(productId);
        }

        Long quantity = cart.getProductQuantity(productId);
        if (quantity == null) {
            throw new IllegalStateException("Product not found in cart.");
        }

        cart.removeItem(productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotExistsException(productId));

        product.setAvailable(product.getAvailable() + quantity);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void changeQuantity(Long productId, Long newQuantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotExistsException(productId));

        Long currentQuantity = cart.getProductQuantity(productId);
        if (currentQuantity == null) {
            throw new IllegalStateException("Product not found in cart.");
        }

        long delta = newQuantity - currentQuantity;

        if (delta > 0) {
            if (product.getAvailable() < delta) {
                throw new NotEnoughProductsAvailableException(productId);
            }
            product.setAvailable(product.getAvailable() - delta);
        } else if (delta < 0) {
            product.setAvailable(product.getAvailable() + Math.abs(delta));
        }

        productRepository.save(product);
        cart.changeQuantity(productId, newQuantity);
    }

    @Transactional
    @Override
    public void checkout() {
        List<CartItemDto> items = cart.getCartItems().values().stream().toList();

        for (CartItemDto item : items) {
            Product product = productRepository.findByTitle(item.getName()).orElseThrow(
                    () -> new ProductNotExistsException(item.getName()));

            if (product == null) {
                throw new ProductNotExistsException(item.getName());
            }

            if (product.getAvailable() < item.getQuantity()) {
                throw new NotEnoughProductsAvailableException(product.getId());
            }

            product.setAvailable(product.getAvailable() - item.getQuantity());
            productRepository.save(product);
        }

        cart.clear();
    }
}

