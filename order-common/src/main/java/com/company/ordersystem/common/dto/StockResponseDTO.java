package com.company.ordersystem.common.dto;

import com.company.ordersystem.common.constant.EStockStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class StockResponseDTO {
    private Long productId;
    private int availableStock;
    private LocalDateTime updatedAt;
    private EStockStatus operationType;
}
