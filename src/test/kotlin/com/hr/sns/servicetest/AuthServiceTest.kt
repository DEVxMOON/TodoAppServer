package com.hr.sns.servicetest

import com.hr.sns.domain.auth.dto.LoginRequest
import com.hr.sns.domain.auth.dto.SignUpRequest
import com.hr.sns.domain.auth.service.AuthService
import com.hr.sns.domain.user.entity.User
import com.hr.sns.domain.user.repository.UserRepository
import com.hr.sns.exception.InvalidPasswordException
import com.hr.sns.exception.UserAlreadyExistsException
import com.hr.sns.exception.UserNotFoundException
import com.hr.sns.infra.security.jwt.JwtPlugin
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

class AuthServiceTest : BehaviorSpec({
    val userRepository = mockk<UserRepository>()
    val jwtPlugin = mockk<JwtPlugin>()
    val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()
    val authService = AuthService(userRepository, passwordEncoder, jwtPlugin)

    val signUpRequest = SignUpRequest("testUser", "test@example.com", "password")
    val loginRequest = LoginRequest("test@example.com", "password")
    val user = User("testUser", "test@example.com", passwordEncoder.encode("password")).apply { id = 1L }


    given("AuthService Test") {
        `when`("register") {
            then("success") {
                every { userRepository.existsByEmail(signUpRequest.email) } returns false
                every { userRepository.save(any<User>()) } returns user

                val userResponse = authService.register(signUpRequest)

                userResponse.email shouldBe signUpRequest.email
                userResponse.name shouldBe signUpRequest.name

                verify { userRepository.save(any<User>()) }

            }

            then("fail - user already exists") {
                every { userRepository.existsByEmail(signUpRequest.email) } returns true

                val exception = shouldThrow<UserAlreadyExistsException> {
                    authService.register(signUpRequest)
                }
                exception.message shouldBe "User with email ${signUpRequest.email} already exists."

                verify { userRepository.existsByEmail(signUpRequest.email) }
            }

        }

        `when`("login") {
            then("success") {
                every { userRepository.findByEmail(loginRequest.email) } returns user
                every { jwtPlugin.generateAccessToken(any(), any(), any()) } returns "accessToken"

                val response = authService.login(loginRequest)
                response.token shouldBe "accessToken"

            }

            then("fail - user not found") {
                every { userRepository.findByEmail(loginRequest.email) } returns null

                val exception = shouldThrow<UserNotFoundException> {
                    authService.login(loginRequest)
                }
                exception.message shouldBe "User with email ${loginRequest.email} not found."

                verify { userRepository.findByEmail(loginRequest.email) }
            }

            then("fail - invalid password") {
                val invalidLoginRequest = LoginRequest("test@example.com", "wrongPassword")
                every { userRepository.findByEmail(invalidLoginRequest.email) } returns user

                val exception = shouldThrow<InvalidPasswordException> {
                    authService.login(invalidLoginRequest)
                }
                exception.message shouldBe "User with email ${invalidLoginRequest.email} does not match password."

                verify { userRepository.findByEmail(invalidLoginRequest.email) }
            }
        }

        `when`("generate Token") {
            then("success") {
                every { jwtPlugin.generateAccessToken(any(), any(), any()) } returns "accessToken"
                every { userRepository.save(any()) } returns user

                val response = authService.generateToken(user)
                response.token shouldBe "accessToken"

                verify { jwtPlugin.generateAccessToken(any(), any(), any()) }
                verify { userRepository.save(any()) }
            }

            then("fail - repository save failure") {
                every { jwtPlugin.generateAccessToken(any(), any(), any()) } returns "accessToken"
                every { userRepository.save(any()) } throws RuntimeException("Database save failed")

                val exception = shouldThrow<RuntimeException> {
                    authService.generateToken(user)
                }
                exception.message shouldBe "Database save failed"

                verify { jwtPlugin.generateAccessToken(any(), any(), any()) }
                verify { userRepository.save(any()) }
            }
        }
    }
})