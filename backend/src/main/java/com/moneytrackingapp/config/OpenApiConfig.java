package com.moneytrackingapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI moneyTrackingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Money Tracking API")
                        .description("REST API for tracking spend entries, budgets, recurring expenses, and profile settings.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Money Tracking App")));
    }
}
