package com.company.ordersystem.common.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReturnRequestDTO {
    @NotNull(message = "orderId cannot be null")
    private Long orderId;
    @NotNull(message = "productId cannot be null")
    private Long productId;
    @NotBlank(message = " a reason is required")
    private String reason;
    @NotNull(message = "quantity cannot be null")
    @Min(value = 1 , message = "quantity must be at least 1")
    private int quantity;
}
