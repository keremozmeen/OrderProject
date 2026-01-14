package com.company.ordersystem.product.controller;

import com.company.ordersystem.common.dto.ProductRequestDTO;
import com.company.ordersystem.common.dto.ProductResponseDTO;
import com.company.ordersystem.common.dto.ResultDTO;
import com.company.ordersystem.product.service.ProductService;
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
@RequestMapping("/api/v1/admin/products")
@RequiredArgsConstructor
@Tag(name = "product-controller", description = "Product management APIs")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get all products")
    @GetMapping
    public ResponseEntity<ResultDTO<List<ProductResponseDTO>>> getAllProducts() {
        log.info("Getting all products");
        List<ProductResponseDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(new ResultDTO<>("00", "Success", products));
    }

    @Operation(summary = "Get product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ResultDTO<ProductResponseDTO>> getProductById(@PathVariable Long id) {
        log.info("Getting product with id: {}", id);
        ProductResponseDTO product = productService.getProductById(id);
        return ResponseEntity.ok(new ResultDTO<>("00", "Success", product));
    }

    @Operation(summary = "Create new product")
    @PostMapping
    public ResponseEntity<ResultDTO<ProductResponseDTO>> createProduct(
            @Valid @RequestBody ProductRequestDTO request) {
        log.info("Creating product: {}", request.getName());
        ProductResponseDTO product = productService.createProduct(request);
        log.info("Product created with id: {}", product.getId());
        return ResponseEntity.ok(new ResultDTO<>("00", "Success", product));
    }

    @Operation(summary = "Update product")
    @PutMapping("/{id}")
    public ResponseEntity<ResultDTO<ProductResponseDTO>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO request) {
        log.info("Updating product with id: {}", id);
        request.setProductId(id);
        ProductResponseDTO product = productService.updateProduct(request);
        log.info("Product updated with id: {}", id);
        return ResponseEntity.ok(new ResultDTO<>("00", "Success", product));
    }

    @Operation(summary = "Delete product")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResultDTO<Void>> deleteProduct(@PathVariable Long id) {
        log.info("Deleting product with id: {}", id);
        productService.deleteProduct(id);
        log.info("Product deleted with id: {}", id);
        return ResponseEntity.ok(new ResultDTO<>("00", "Success", null));
    }

    @Operation(summary = "Update product stock")
    @PutMapping("/{id}/stock")
    public ResponseEntity<ResultDTO<ProductResponseDTO>> updateStock(
            @PathVariable Long id,
            @RequestParam Integer stock) {
        log.info("Updating stock for product id: {} to {}", id, stock);
        ProductResponseDTO product = productService.updateStock(id, stock);
        return ResponseEntity.ok(new ResultDTO<>("00", "Success", product));
    }
}
