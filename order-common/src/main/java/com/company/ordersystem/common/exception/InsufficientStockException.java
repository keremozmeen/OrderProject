package com.company.ordersystem.common.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientStockException extends RuntimeException {
    private final Long productId;
    private final int requestedQuantity;
    private final int availableStock;

    public InsufficientStockException(Long productId, int requestedQuantity, int availableStock) {
        super(String.format("Not enough stock for product id: %d", productId));
        this.productId = productId;
        this.requestedQuantity = requestedQuantity;
        this.availableStock = availableStock;
    }
}

