package com.company.ordersystem.order.controller;

import com.company.ordersystem.common.dto.OrderRequestDTO;
import com.company.ordersystem.common.dto.OrderResponseDTO;
import com.company.ordersystem.common.dto.OrderStatusUpdateRequestDTO;
import com.company.ordersystem.common.dto.ResultDTO;
import com.company.ordersystem.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "order-controller", description = "Order management APIs")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Create new order (Customer)")
    @PostMapping("/api/v1/customer/orders")
    public ResponseEntity<ResultDTO<OrderResponseDTO>> createOrder(
            @Valid @RequestBody OrderRequestDTO request) {
        log.info("Creating new order");
        OrderResponseDTO order = orderService.createOrder(request);
        return ResponseEntity.ok(new ResultDTO<>("00", "Success", order));
    }

    @Operation(summary = "Get all orders (Admin)")
    @GetMapping("/api/v1/admin/orders")
    public ResponseEntity<ResultDTO<List<OrderResponseDTO>>> getAllOrders() {
        log.info("Getting all orders");
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(new ResultDTO<>("00", "Success", orders));
    }

    // Diğer metodlar aynı kalabilir...
}