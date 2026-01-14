package com.company.ordersystem.common.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_items")
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    private Long productId;

    private Integer quantity;

    private Double price;
}
