package com.hr.todoapp.infra.security.config

import com.hr.todoapp.infra.security.CustomAuthenticationEntryPoint
import com.hr.todoapp.infra.security.jwt.JwtAuthenticationFilter
import com.hr.todoapp.infra.swagger.SwaggerConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val authenticationEntryPoint: CustomAuthenticationEntryPoint
) {
    @Bean
    fun filterChain(http: HttpSecurity, swaggerConfig: SwaggerConfig): SecurityFilterChain{
        return http
            .httpBasic{it.disable()}
            .formLogin{it.disable()}
            .csrf{it.disable()}
            .authorizeHttpRequests{
                it.requestMatchers(
                    "/auth/**",
                    "/oauth/**",
                    "/swagger-ui/**",
                    "v3/api-docs/**",
                ).permitAll().anyRequest().permitAll()
            }.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling{
                it.authenticationEntryPoint(authenticationEntryPoint)
            }.build()
    }
}