package org.example.springwebcapstoneproject.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.springwebcapstoneproject.dto.error.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorDto> buildError(HttpStatus status, String message, HttpServletRequest request) {
        ErrorDto error = ErrorDto.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(message)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorDto> handleUserAlreadyExists(UserAlreadyExistsException ex,
                                                            HttpServletRequest request) {
        log.warn("UserAlreadyExistsException at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFound(UserNotFoundException ex,
                                                       HttpServletRequest request) {
        log.warn("UserNotFoundException at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<ErrorDto> handleWrongPassword(WrongPasswordException ex,
                                                        HttpServletRequest request) {
        log.warn("WrongPasswordException at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildError(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(ProductNotExistsException.class)
    public ResponseEntity<ErrorDto> handleProductNotFound(ProductNotExistsException ex,
                                                          HttpServletRequest request) {
        log.warn("ProductNotExistsException at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(NotEnoughProductsAvailableException.class)
    public ResponseEntity<ErrorDto> handleNotEnoughAvailability(NotEnoughProductsAvailableException ex,
                                                                HttpServletRequest request) {
        log.warn("NotEnoughProductsAvailableException at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(UserNotLoggedInException.class)
    public ResponseEntity<ErrorDto> handleUnauthorized(UserNotLoggedInException ex,
                                                       HttpServletRequest request) {
        log.warn("UserNotLoggedInException at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildError(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorDto> handleAuthenticationException(AuthenticationException ex,
                                                                  HttpServletRequest request) {
        log.warn("AuthenticationException at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildError(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleAccessDenied(AccessDeniedException ex,
                                                       HttpServletRequest request) {
        log.warn("AccessDeniedException at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildError(HttpStatus.FORBIDDEN, ex.getMessage(), request);
    }
}
