package com.hr.todoapp.domain.auth.service

import com.hr.todoapp.domain.auth.dto.LoginRequest
import com.hr.todoapp.domain.auth.dto.PasswordChangeRequest
import com.hr.todoapp.domain.auth.dto.SignUpRequest
import com.hr.todoapp.domain.user.dto.UserResponse
import com.hr.todoapp.domain.user.entity.User
import com.hr.todoapp.domain.user.repository.UserRepository
import com.hr.todoapp.infra.security.jwt.JwtPlugin
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin
) {
    fun register(signUpRequest: SignUpRequest): UserResponse {
        if (userRepository.existsByEmail(signUpRequest.email)) {
            throw Exception("User with email ${signUpRequest.email} already exists.")
        }

        val user = User(
            name = signUpRequest.name,
            email = signUpRequest.email,
            pw = signUpRequest.pw,
        )
        userRepository.save(user)
        return UserResponse.from(user)
    }

    fun login(loginRequest: LoginRequest): String {
        val user = userRepository.findByEmail(loginRequest.email)
            ?: throw Exception("User with email ${loginRequest.email} not found.")

        if (!passwordEncoder.matches(loginRequest.pw, user.pw)) {
            throw Exception("User with email ${loginRequest.email} does not match password.")
        }

        return jwtPlugin.generateAccessToken(
            subject = user.id.toString(),
            email = user.email,
            role = "User"
        )
    }

    fun passwordChange(request: PasswordChangeRequest,userId:Long): String {
        val user = userRepository.findByIdOrNull(userId) ?: throw Exception("User with id $userId not found.")

        val newPasswordEncoder = passwordEncoder.encode(request.newPw)

        if(!passwordEncoder.matches(request.oldPw, user.pw)) {
            throw Exception("User with id ${user.id} does not match password.")
        }
        if(passwordEncoder.matches(request.newPw,request.oldPw)){
            throw Exception("Password does not changed")
        }

        user.pw = newPasswordEncoder

        return "Password Changed Successfully!"
    }
}