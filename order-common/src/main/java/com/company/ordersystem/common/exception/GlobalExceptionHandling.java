package com.company.ordersystem.common.exception;

import com.company.ordersystem.common.dto.ResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandling {

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ResultDTO> handleInsufficientStock(
            InsufficientStockException ex, HttpServletRequest request) {

        Map<String, Object> details = Map.of(
                "productId", ex.getProductId(),
                "requestedQuantity", ex.getRequestedQuantity(),
                "availableStock", ex.getAvailableStock()
        );

        ResultDTO response = ResultDTO.builder()
                .code("98")
                .data(null)
                .message(ex.getMessage())
                .build();

        log.warn("Insufficient stock exception: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ResultDTO> handleProductNotFound(
            ProductNotFoundException ex, HttpServletRequest request) {

        ResultDTO response = ResultDTO.builder()
                .code("97")
                .data(null)
                .message(ex.getMessage())
                .build();

        log.warn("Product not found exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResultDTO> handleUserNotFound(
            UserNotFoundException ex, HttpServletRequest request) {

        ResultDTO response = ResultDTO.builder()
                .code("96")
                .data(null)
                .message(ex.getMessage())
                .build();

        log.warn("User not found exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ResultDTO> handleOrderNotFound(
            OrderNotFoundException ex, HttpServletRequest request) {

        ResultDTO response = ResultDTO.builder()
                .code("95")
                .data(null)
                .message(ex.getMessage())
                .build();

        log.warn("Order not found exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResultDTO> handleAccessDenied(
            AccessDeniedException ex, HttpServletRequest request) {

        ResultDTO response = ResultDTO.builder()
                .code("94")
                .data(null)
                .message(ex.getMessage())
                .build();

        log.warn("Access denied exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResultDTO> handleRuntimeException(
            RuntimeException ex, HttpServletRequest request) {

        ResultDTO response = ResultDTO.builder()
                .code("93")
                .data(null)
                .message(ex.getMessage())
                .build();

        log.error("Runtime exception: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResultDTO> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {


        ResultDTO response = ResultDTO.builder()
                .code("92")
                .data(null)
                .message(ex.getMessage())
                .build();

        log.error("Illegal Argument exception: ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResultDTO> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request){

        ResultDTO response = ResultDTO.builder()
                .code("91")
                .data(null)
                .message("HTTP Method not supported")
                .build();

        log.error("Method Not Supported exception: {}, supported methods: {}", ex.getMethod(),ex.getSupportedHttpMethods());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);

    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultDTO<Void>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        ResultDTO<Void> response = ResultDTO.<Void>builder()
                .code("90")
                .data(null)
                .message(errorMessage)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}