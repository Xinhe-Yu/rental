package com.chatop.rental.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Chatop Rental API")
                        .version("1.0")
                        .description("API Documentation for Chatop Rental Application")
                        .contact(new Contact()
                                .name("Xinhe YU")
                                .email("xinhe.yu.dsa@gmail.com")));
    }
}
