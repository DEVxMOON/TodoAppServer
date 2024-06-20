package com.hr.todoapp.infra.swagger

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI{
        return OpenAPI().
        info(
            Info().
            title("Todo App Server").
            description("Todo App API Schema").
            version("v1.0.0")
        )
    }
}