package com.company.ordersystem.return_;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Return Service Application
 */
@SpringBootApplication(scanBasePackages = {
        "com.company.ordersystem.return_",
        "com.company.ordersystem.common"
})
public class ReturnServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReturnServiceApplication.class, args);
    }
}