package com.company.ordersystem.return_.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI getOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ordersystem Return Service")
                        .version("1.0")
                        .description("Return management APIs for Ordersystem")
                )
                .addSecurityItem(new SecurityRequirement().addList("OrdersystemSecurityScheme"))
                .components(new Components()
                        .addSecuritySchemes("OrdersystemSecurityScheme",
                                new SecurityScheme()
                                        .name("OrdersystemSecurityScheme")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}
