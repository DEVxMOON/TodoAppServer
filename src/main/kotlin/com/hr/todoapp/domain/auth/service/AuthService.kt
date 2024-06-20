package com.hr.todoapp.domain.auth.service

import com.hr.todoapp.domain.auth.dto.LoginRequest
import com.hr.todoapp.domain.auth.dto.PasswordChangeRequest
import com.hr.todoapp.domain.auth.dto.SignUpRequest
import com.hr.todoapp.domain.user.dto.UserResponse
import org.springframework.stereotype.Service

@Service
class AuthService {

    fun register(signUpRequest: SignUpRequest):UserResponse{
        TODO()
    }

    fun login(loginRequest: LoginRequest):UserResponse{
        TODO()
    }

    fun passwordChange(passwordChangeRequest: PasswordChangeRequest):String{
        TODO()
    }
}