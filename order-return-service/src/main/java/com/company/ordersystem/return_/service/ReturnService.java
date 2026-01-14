package com.company.ordersystem.return_.service;

import com.company.ordersystem.common.constant.EReturnStatus;
import com.company.ordersystem.common.dto.ReturnRequestDTO;
import com.company.ordersystem.common.dto.ReturnResponseDTO;
import com.company.ordersystem.common.dto.ReturnStatusUpdateRequestDTO;

import java.util.List;

public interface ReturnService {

    ReturnResponseDTO createReturn(ReturnRequestDTO request);

    ReturnResponseDTO getReturnById(Long id);

    List<ReturnResponseDTO> getReturnsByOrderId(Long orderId);

    List<ReturnResponseDTO> getCustomerReturns();

    List<ReturnResponseDTO> getAllReturns();

    ReturnResponseDTO updateReturnStatus(ReturnStatusUpdateRequestDTO requestDTO);

    void deleteReturn(Long id);
}
