package com.company.ordersystem.order.service;

import com.company.ordersystem.common.dto.OrderRequestDTO;
import com.company.ordersystem.common.dto.OrderResponseDTO;
import com.company.ordersystem.common.dto.OrderStatusUpdateRequestDTO;

import java.util.List;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO request);
    OrderResponseDTO getOrderById(Long id);
    List<OrderResponseDTO> getCustomerOrders();
    List<OrderResponseDTO> getAllOrders();
    OrderResponseDTO updateOrderStatus(OrderStatusUpdateRequestDTO request);
    void cancelOrder(Long id);
    void deleteOrder(Long id);
}
