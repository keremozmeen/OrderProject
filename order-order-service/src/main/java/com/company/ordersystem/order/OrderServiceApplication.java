package com.company.ordersystem.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Order Service Application
 * 
 * Handles:
 * - Order creation and management
 * - Order status transitions
 * - Order validation
 */
@SpringBootApplication(scanBasePackages = {
    "com.company.ordersystem.order",
    "com.company.ordersystem.common"
})

public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
