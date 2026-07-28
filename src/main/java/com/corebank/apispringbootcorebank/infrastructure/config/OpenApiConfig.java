package com.corebank.apispringbootcorebank.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI coreBankOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("CoreBank API")
                        .description("REST API for account creation, balance queries and debit transactions.")
                        .version("v1")
                        .contact(new Contact()
                                .name("CoreBank Engineering"))
                        .license(new License()
                                .name("Portfolio project")));
    }
}
