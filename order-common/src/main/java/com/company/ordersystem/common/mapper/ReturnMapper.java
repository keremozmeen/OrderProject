package com.company.ordersystem.common.mapper;

import com.company.ordersystem.common.dto.ReturnRequestDTO;
import com.company.ordersystem.common.dto.ReturnResponseDTO;
import com.company.ordersystem.common.entity.ReturnEntity;
import org.mapstruct.Mapper;
import java.util.List;
@Mapper(componentModel = "spring")
public interface ReturnMapper {

    // DTO -> Entity
    ReturnEntity toEntity(ReturnRequestDTO dto);

    ReturnResponseDTO toResponseDTO(ReturnEntity entity);

    List<ReturnResponseDTO> toResponseDTOList(List<ReturnEntity> entities);
}
