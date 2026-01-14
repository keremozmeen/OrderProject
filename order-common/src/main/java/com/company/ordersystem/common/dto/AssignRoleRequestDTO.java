package com.company.ordersystem.common.dto;

import com.company.ordersystem.common.constant.ERole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignRoleRequestDTO {
    @NotNull(message = "userId cannot be null")
    private Long userId;
    @NotNull(message = "role cannot be null")
    private ERole role;
}
