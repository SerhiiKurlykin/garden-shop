package com.predators.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    private static final String BEARER_AUTH_SCHEME_NAME = "bearerAuth";
    private static final String BASIC_AUTH_SCHEME_NAME = "basicAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Online Shop API")
                        .description("API for Online Shop application")
                        .version("v1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH_SCHEME_NAME).addList(BASIC_AUTH_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH_SCHEME_NAME, new SecurityScheme()
                                .name(BEARER_AUTH_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter JWT token without Bearer prefix, e.g. 'abcde12345'"))
                        .addSecuritySchemes(BASIC_AUTH_SCHEME_NAME, new SecurityScheme()
                                .name(BASIC_AUTH_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")
                                .description("Enter your username and password for Basic Authentication")));
    }
}