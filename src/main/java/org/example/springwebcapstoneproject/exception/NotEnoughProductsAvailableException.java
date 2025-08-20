package org.example.springwebcapstoneproject.exception;

public class NotEnoughProductsAvailableException extends RuntimeException {
    public NotEnoughProductsAvailableException(Long productId) {
      super("Product with id '" + productId + "' has not enough availability.");
    }
}
