package com.company.ordersystem.common.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CategoryRequestDTO {
    @NotEmpty(message = "name cannot be empty")
    private String name;
}
