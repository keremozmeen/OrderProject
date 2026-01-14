package com.company.ordersystem.common.dto;

import com.company.ordersystem.common.constant.EOrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderRequestDTO {
    @NotEmpty(message = "items cannot be null")
    private List<OrderItemDTO> items = new ArrayList<>();

}
