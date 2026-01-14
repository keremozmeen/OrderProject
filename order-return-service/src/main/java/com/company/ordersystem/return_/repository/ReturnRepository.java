package com.company.ordersystem.return_.repository;

import com.company.ordersystem.common.entity.ReturnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnRepository extends JpaRepository<ReturnEntity, Long> {
    List<ReturnEntity> findByOrderId(Long orderId);
}
