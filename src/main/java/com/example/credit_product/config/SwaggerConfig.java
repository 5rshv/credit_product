package com.example.credit_product.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        System.out.println("SwaggerConfig initialized!");
        return new OpenAPI()
                .info(new Info()
                        .title("Bank Recommendations API")
                        .version("1.0")
                        .description("API для системы рекомендаций банковских продуктов"));
    }
}