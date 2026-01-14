package com.company.ordersystem.common.mapper;

import com.company.ordersystem.common.dto.ProductRequestDTO;
import com.company.ordersystem.common.dto.ProductResponseDTO;
import com.company.ordersystem.common.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    ProductEntity toEntity(ProductRequestDTO dto);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    ProductResponseDTO toResponseDTO(ProductEntity entity);

    List<ProductResponseDTO> toResponseDTOList(List<ProductEntity> entities);
}
