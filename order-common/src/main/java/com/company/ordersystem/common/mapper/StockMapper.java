package com.company.ordersystem.common.mapper;

import com.company.ordersystem.common.dto.StockResponseDTO;
import com.company.ordersystem.common.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockMapper {

    @Mapping(target = "productId", source = "id")
    @Mapping(target = "availableStock", source = "stock")
    StockResponseDTO toResponseDTO(ProductEntity product);
}
