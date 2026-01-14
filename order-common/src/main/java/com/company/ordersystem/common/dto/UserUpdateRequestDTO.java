package com.company.ordersystem.common.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequestDTO {
    @NotNull(message = "id cannot be null")
    private Long id;
    @NotBlank(message = "firstName cannot be blank")
    private String firstName;
    @NotBlank(message = "lastName cannot be blank")
    private String lastName;
    @Email(message = "Invalid email format")
    @NotBlank(message = "email cannot be blank")
    private String email;
    @NotBlank(message = "password cannot be blank")
    private String password;

}
