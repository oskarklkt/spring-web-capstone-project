package org.example.springwebcapstoneproject.exception;

public class ProductNotExistsException extends RuntimeException {
    public ProductNotExistsException(Long productId) {
        super("Product with id '" + productId + "' doesn't exists.");
    }
    public ProductNotExistsException(String productName) {
        super("Product with name '" + productName + "' doesn't exists.");
    }
}
