package com.company.ordersystem.common.dto;

import com.company.ordersystem.common.constant.ERole;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class UserSignupRequestDTO {

    @NotBlank(message = "Email is required")
    @NotNull
    @Size(max = 50)
    @Email
    private String email;

    @NotNull
    @NotBlank(message = "Name Is Required")
    private String firstName;

    @NotNull
    @NotBlank(message = "Surname is required")
    private String lastName;

    @NotNull
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 40)
    private String password;

    @NotNull
    @NotBlank(message = "Phone number is required")
    @Size(max = 10)
    private String phoneNumber;

}
