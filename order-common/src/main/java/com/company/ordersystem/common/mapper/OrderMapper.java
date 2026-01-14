package com.company.ordersystem.common.mapper;

import com.company.ordersystem.common.dto.OrderRequestDTO;
import com.company.ordersystem.common.dto.OrderResponseDTO;
import com.company.ordersystem.common.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    // RequestDTO -> Entity
    @Mapping(target = "id", ignore = true) // create sırasında id DB tarafından setlenecek
    @Mapping(target = "totalPrice", ignore = true) // service içinde hesaplanacak
    @Mapping(target = "status", ignore = true) // service default "CREATED" yapacak
    @Mapping(target = "createdAt", ignore = true) // otomatik setlenecek
    OrderEntity toEntity(OrderRequestDTO dto);

    //@Mapping(source = "orderStatus",target = "status")
    @Mapping(source = "items",target = "items")
    OrderResponseDTO toResponseDTO(OrderEntity entity);

    List<OrderResponseDTO> toResponseDTOList(List<OrderEntity> entities);
}
