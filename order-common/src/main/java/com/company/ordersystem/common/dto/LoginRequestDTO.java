package com.company.ordersystem.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotBlank(message = "email cannot be blank")
    private String email;
    @NotBlank(message = "password cannot be blank")
    private String password;
}
