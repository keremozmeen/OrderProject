package com.company.ordersystem.common.dto;

import com.company.ordersystem.common.constant.EOrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderStatusUpdateRequestDTO {
    @NotNull(message = "id cannot be null")
    private Long id;
    @NotNull(message = "Status can't be null")
    private EOrderStatus status;
}
