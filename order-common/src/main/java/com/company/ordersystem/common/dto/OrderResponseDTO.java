package com.company.ordersystem.common.dto;

import com.company.ordersystem.common.constant.EOrderStatus;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDTO {
    private Long id;
    private Long customerId;
    private Double totalPrice;
    private EOrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemDTO> items;
}
