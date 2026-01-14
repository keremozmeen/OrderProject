package com.company.ordersystem.common.dto;

import com.company.ordersystem.common.constant.EStockStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockRequestDTO {

    @NotNull(message = "productId cannot be null")
    private Long productId;

    @NotNull(message = "quantity cannot be null")
    @Min(value = 1, message = "quantity must be greater than 0")
    private Integer quantity;

    @NotNull(message = "operation type cannot be null")
    private EStockStatus operationType;
}