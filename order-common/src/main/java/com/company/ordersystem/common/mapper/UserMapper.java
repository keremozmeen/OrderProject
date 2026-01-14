package com.company.ordersystem.common.mapper;

import com.company.ordersystem.common.constant.ERole;
import com.company.ordersystem.common.dto.UserResponseDTO;
import com.company.ordersystem.common.dto.UserSignupRequestDTO;
import com.company.ordersystem.common.entity.RoleEntity;
import com.company.ordersystem.common.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // DTO -> Entity (roller service tarafında setlenecek)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    UserEntity toEntity(UserSignupRequestDTO dto);

    // Entity -> Response DTO
    @Mapping(source = "roles", target = "roles")
    @Mapping(source="phoneNumber",target = "phoneNumber")
    UserResponseDTO toResponseDTO(UserEntity entity);

    // RoleEntity -> String
    default Set<ERole> mapRoles(Set<RoleEntity> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toSet());
    }
}
