package com.company.ordersystem.product.repository;

import com.company.ordersystem.common.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    @Modifying
    @Query("UPDATE ProductEntity p SET p.stock = p.stock + :quantity WHERE p.id = :productId")
    void IncrementStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);
}
