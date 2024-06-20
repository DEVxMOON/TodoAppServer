package com.hr.todoapp.domain.auth.controller

import com.hr.todoapp.domain.auth.dto.LoginRequest
import com.hr.todoapp.domain.auth.dto.PasswordChangeRequest
import com.hr.todoapp.domain.auth.dto.SignUpRequest
import com.hr.todoapp.domain.auth.service.AuthService
import com.hr.todoapp.domain.user.dto.UserResponse
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
    fun login( @RequestBody loginRequest: LoginRequest): ResponseEntity<UserResponse>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(authService.login(loginRequest))
    }

    @PatchMapping("/password-change")
    fun changePassword(
        @RequestBody passwordChangeRequest: PasswordChangeRequest
    ):ResponseEntity<String>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(authService.passwordChange(passwordChangeRequest))
    }
}