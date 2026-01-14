package com.company.ordersystem.product.service;

import com.company.ordersystem.common.dto.ProductRequestDTO;
import com.company.ordersystem.common.dto.ProductResponseDTO;

import java.util.List;

public interface ProductService {
    List<ProductResponseDTO> getAllProducts();
    ProductResponseDTO getProductById(Long id);
    ProductResponseDTO createProduct(ProductRequestDTO dto);
    ProductResponseDTO updateProduct(ProductRequestDTO dto);
    void deleteProduct(Long id);
    ProductResponseDTO updateStock(Long id, int stock);
}
