package com.hr.sns.domain.auth.service

import com.hr.sns.domain.auth.dto.LoginRequest
import com.hr.sns.domain.auth.dto.SignUpRequest
import com.hr.sns.domain.auth.dto.TokenResponse
import com.hr.sns.domain.user.dto.UserResponse
import com.hr.sns.domain.user.entity.User
import com.hr.sns.domain.user.repository.UserRepository
import com.hr.sns.exception.InvalidPasswordException
import com.hr.sns.exception.UserAlreadyExistsException
import com.hr.sns.exception.UserNotFoundException
import com.hr.sns.infra.security.jwt.JwtPlugin
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
            throw UserAlreadyExistsException("User with email ${signUpRequest.email} already exists.")
        }

        val user = User(
            name = signUpRequest.name,
            email = signUpRequest.email,
            pw = signUpRequest.pw,
        )
        userRepository.save(user)
        return UserResponse.from(user)
    }

    fun login(loginRequest: LoginRequest): TokenResponse {
        val user = userRepository.findByEmail(loginRequest.email)
            ?: throw UserNotFoundException("User with email ${loginRequest.email} not found.")

        if (!passwordEncoder.matches(loginRequest.pw, user.pw)) {
            throw InvalidPasswordException("User with email ${loginRequest.email} does not match password.")
        }

        return generateToken(user)
    }

    fun generateToken(user:User): TokenResponse {
        val accessToken = jwtPlugin.generateAccessToken(
            subject = user.id.toString(),
            email = user.email,
            role = "User"
        )

        userRepository.save(user)
        return TokenResponse(token = accessToken)
    }

}