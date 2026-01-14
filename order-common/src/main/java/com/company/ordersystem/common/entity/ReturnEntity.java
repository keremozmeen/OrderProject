package com.company.ordersystem.common.entity;

import com.company.ordersystem.common.constant.EReturnStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "returns")
@Data
@NoArgsConstructor
public class ReturnEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    private String reason;

    @Enumerated(EnumType.STRING)
    private EReturnStatus status = EReturnStatus.REQUESTED;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
