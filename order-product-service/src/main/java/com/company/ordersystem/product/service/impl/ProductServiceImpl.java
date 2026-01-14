package com.company.ordersystem.product.service.impl;

import com.company.ordersystem.common.dto.ProductRequestDTO;
import com.company.ordersystem.common.dto.ProductResponseDTO;
import com.company.ordersystem.common.entity.CategoryEntity;
import com.company.ordersystem.common.entity.ProductEntity;
import com.company.ordersystem.common.mapper.ProductMapper;
import com.company.ordersystem.product.repository.CategoryRepository;
import com.company.ordersystem.product.repository.ProductRepository;
import com.company.ordersystem.product.service.ProductService;
import org.springframework.util.Assert;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        List<ProductEntity> products = productRepository.findAll();
        return productMapper.toResponseDTOList(products);
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        ProductEntity product = productRepository.findById(id).orElse(null);
        Assert.notNull(product , "Product not found");
        return productMapper.toResponseDTO(product);
    }

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        Assert.isTrue(dto.getStock() > 0, "Stock must be greater than 0 when creating a product");

        ProductEntity entity = productMapper.toEntity(dto);

        CategoryEntity category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
        Assert.notNull(category , "Category not found");
        entity.setCategory(category);

        ProductEntity saved = productRepository.save(entity);
        return productMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(ProductRequestDTO dto) {
        ProductEntity product = productRepository.findById(dto.getProductId()).orElse(null);
        Assert.notNull(product , "Product not found");

        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getDescription() != null) product.setDescription(dto.getDescription());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getStock() != null) product.setStock(dto.getStock());

        if(dto.getCategoryId() != null){
            CategoryEntity category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
            Assert.notNull(category , "Category Not Found");
            product.setCategory(category);
        }

        ProductEntity updated = productRepository.save(product);
        return productMapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Assert.isTrue(productRepository.existsById(id), "Product not found with id: " + id);

        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ProductResponseDTO updateStock(Long id, int stock) {
        ProductEntity product = productRepository.findById(id).orElse(null);
        Assert.notNull(product , "Product not found");

        product.setStock(stock);
        ProductEntity updated = productRepository.save(product);

        return productMapper.toResponseDTO(updated);
    }
}
