package com.hr.sns.domain.auth.controller

import com.hr.sns.domain.auth.dto.LoginRequest
import com.hr.sns.domain.auth.dto.SignUpRequest
import com.hr.sns.domain.auth.dto.TokenResponse
import com.hr.sns.domain.auth.service.AuthService
import com.hr.sns.domain.user.dto.UserResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController (private val authService: AuthService) {

    @PostMapping("/register")
    fun register( @RequestBody signUpRequest: SignUpRequest): ResponseEntity<UserResponse>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(authService.register(signUpRequest))
    }

    @PostMapping("/login")
    fun login( @RequestBody loginRequest: LoginRequest): ResponseEntity<TokenResponse>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(authService.login(loginRequest))
    }

}