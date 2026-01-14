package com.company.ordersystem.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * User Service Application
 * 
 * Handles:
 * - User registration and authentication
 * - User profile management
 * - Role-based access control
 * - JWT token generation
 */
@SpringBootApplication(scanBasePackages = {
    "com.company.ordersystem.user",
    "com.company.ordersystem.common"
})

public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
