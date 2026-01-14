package com.company.ordersystem.common.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {
    @NotNull(message = "productId cannot be null")
    private Long productId;
    @NotEmpty(message = "name cannot be empty")
    private String name;
    @NotEmpty(message = "description cannot be empty")
    private String description;
    private Double price;
    @NotNull(message = "stock cannot be null")
    @Min(value = 0 , message = "stock must be at least 0")
    private Integer stock;
    @NotNull(message = "categoryId cannot be null")
    private Long categoryId;
}
