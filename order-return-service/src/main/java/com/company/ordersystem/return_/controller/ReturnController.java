package com.company.ordersystem.return_.controller;

import com.company.ordersystem.common.dto.ReturnRequestDTO;
import com.company.ordersystem.common.dto.ReturnResponseDTO;
import com.company.ordersystem.common.dto.ReturnStatusUpdateRequestDTO;
import com.company.ordersystem.common.dto.ResultDTO;
import com.company.ordersystem.return_.service.ReturnService;
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
@Tag(name = "return-controller", description = "Return management APIs")
public class ReturnController {

    private final ReturnService returnService;

    @Operation(summary = "Create return request (Customer)")
    @PostMapping("/api/v1/customer/returns")
    public ResponseEntity<ResultDTO<ReturnResponseDTO>> createReturn(
            @Valid @RequestBody ReturnRequestDTO request) {
        log.info("Creating return request for order id: {}", request.getOrderId());
        ReturnResponseDTO returnRequest = returnService.createReturn(request);
        log.info("Return request created with id: {}", returnRequest.getId());
        return ResponseEntity.ok(new ResultDTO<>("00", "Success", returnRequest));
    }

    @Operation(summary = "Get customer returns")
    @GetMapping("/api/v1/customer/returns")
    public ResponseEntity<ResultDTO<List<ReturnResponseDTO>>> getCustomerReturns() {
        log.info("Getting customer returns");
        List<ReturnResponseDTO> returns = returnService.getCustomerReturns();
        return ResponseEntity.ok(new ResultDTO<>("00", "Success", returns));
    }

    @Operation(summary = "Get return by ID")
    @GetMapping("/api/v1/customer/returns/{id}")
    public ResponseEntity<ResultDTO<ReturnResponseDTO>> getReturnById(@PathVariable Long id) {
        log.info("Getting return with id: {}", id);
        ReturnResponseDTO returnRequest = returnService.getReturnById(id);
        return ResponseEntity.ok(new ResultDTO<>("00", "Success", returnRequest));
    }

    @Operation(summary = "Get returns by order ID")
    @GetMapping("/api/v1/customer/returns/order/{orderId}")
    public ResponseEntity<ResultDTO<List<ReturnResponseDTO>>> getReturnsByOrderId(
            @PathVariable Long orderId) {
        log.info("Getting returns for order id: {}", orderId);
        List<ReturnResponseDTO> returns = returnService.getReturnsByOrderId(orderId);
        return ResponseEntity.ok(new ResultDTO<>("00", "Success", returns));
    }

    @Operation(summary = "Get all returns (Admin)")
    @GetMapping("/api/v1/admin/returns")
    public ResponseEntity<ResultDTO<List<ReturnResponseDTO>>> getAllReturns() {
        log.info("Getting all returns");
        List<ReturnResponseDTO> returns = returnService.getAllReturns();
        return ResponseEntity.ok(new ResultDTO<>("00", "Success", returns));
    }

    @Operation(summary = "Update return status (Admin)")
    @PutMapping("/api/v1/admin/returns/{id}/status")
    public ResponseEntity<ResultDTO<ReturnResponseDTO>> updateReturnStatus(
            @PathVariable Long id,
            @Valid @RequestBody ReturnStatusUpdateRequestDTO request) {
        log.info("Updating return status for return id: {} to {}", id, request.getStatus());
        request.setId(id);
        ReturnResponseDTO returnRequest = returnService.updateReturnStatus(request);
        log.info("Return status updated for return id: {}", id);
        return ResponseEntity.ok(new ResultDTO<>("00", "Success", returnRequest));
    }

    @Operation(summary = "Delete return (Admin)")
    @DeleteMapping("/api/v1/admin/returns/{id}")
    public ResponseEntity<ResultDTO<Void>> deleteReturn(@PathVariable Long id) {
        log.info("Deleting return with id: {}", id);
        returnService.deleteReturn(id);
        log.info("Return deleted with id: {}", id);
        return ResponseEntity.ok(new ResultDTO<>("00", "Success", null));
    }
}
