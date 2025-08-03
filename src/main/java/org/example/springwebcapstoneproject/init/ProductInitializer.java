package org.example.springwebcapstoneproject.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.springwebcapstoneproject.entity.Product;
import org.example.springwebcapstoneproject.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductInitializer {

    private final ProductRepository productRepository;

    @PostConstruct
    public void insertInitialProducts() {
        if (productRepository.count() == 0) {
            List<Product> products = List.of(
                    Product.builder().title("Nail gun").available(8L).price(23.95).build(),
                    Product.builder().title("Hammer").available(12L).price(12.50).build(),
                    Product.builder().title("Screwdriver Set").available(20L).price(15.99).build()
            );

            productRepository.saveAll(products);
        }
    }
}
