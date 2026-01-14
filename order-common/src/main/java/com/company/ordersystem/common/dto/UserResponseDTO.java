package com.company.ordersystem.common.dto;

import com.company.ordersystem.common.constant.ERole;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class UserResponseDTO {

    private Long id ;

    private String email;

    private String firstName;

    private String lastName;

    private Set<ERole> roles;

    private String phoneNumber;

    private LocalDateTime createdAt ;

    private LocalDateTime updatedAt ;

}
