package com.company.ordersystem.user.repository;

import com.company.ordersystem.common.constant.ERole;
import com.company.ordersystem.common.entity.RoleEntity;
import com.company.ordersystem.common.constant.ERole;
import com.company.ordersystem.common.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(ERole name);
}
