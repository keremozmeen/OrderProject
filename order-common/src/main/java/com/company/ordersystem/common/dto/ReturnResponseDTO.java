package com.company.ordersystem.common.dto;

import com.company.ordersystem.common.constant.EReturnStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReturnResponseDTO {
    private Long id;
    private Long orderId;
    private String reason;
    private EReturnStatus status;
    private LocalDateTime createdAt;
}
