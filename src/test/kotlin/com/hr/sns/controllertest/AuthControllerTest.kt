package com.hr.sns.controllertest

import com.hr.sns.domain.auth.controller.AuthController
import com.hr.sns.domain.auth.dto.LoginRequest
import com.hr.sns.domain.auth.dto.SignUpRequest
import com.hr.sns.domain.auth.dto.TokenResponse
import com.hr.sns.domain.auth.service.AuthService
import com.hr.sns.domain.user.dto.UserResponse
import io.kotest.assertions.fail
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus

class AuthControllerTest : BehaviorSpec({

    val authService = mockk<AuthService>()
    val authController = AuthController(authService)

    given("로그인"){
        val loginRequest = LoginRequest(email = "test@test.com", pw = "password")
        `when`("성공"){
            val tokenResponse = TokenResponse(token="testToken")
            every{authService.login(loginRequest) } returns tokenResponse

            then("결과 : OK status, token Response"){
                val response = authController.login(loginRequest)
                response.statusCode shouldBe HttpStatus.OK
                response.body shouldBe tokenResponse
            }
        }
        `when`("실패"){
            every{authService.login(loginRequest) } throws RuntimeException("LoginError")
            then("결과 : Exception"){
                try{
                    authController.login(loginRequest)
                    fail("exception should have been thrown")
                }catch(e: RuntimeException){
                    e.message shouldBe "LoginError"
                }
            }
        }
    }

    given("회원 가입"){
        val signUpRequest = SignUpRequest(name ="test",email = "test@test.com", pw = "password")
        `when`("성공"){
            val userResponse = UserResponse(id = 1L, name = "test", email = "test@test.com")
            every{ authService.register(signUpRequest)} returns userResponse

            then("결과: OK status, user Response"){
                val response = authController.register(signUpRequest)
                response.statusCode shouldBe HttpStatus.OK
                response.body shouldBe userResponse
            }
        }
        `when`("아이템 중 하나라도 null"){
            val invalidSignUpRequest = SignUpRequest(name = null.toString(), email = "test@test.com", pw = "password")
            every{authService.register(invalidSignUpRequest) } throws RuntimeException("InvalidSignUpError")

            then("결과 : BadRequest status, Error Msg"){
                try{
                    authController.register(invalidSignUpRequest)
                    fail("exception should have been thrown")
                }catch(e: RuntimeException){
                    e.message shouldBe "InvalidSignUpError"
                }
            }
        }
        `when`("이메일 중복"){
            every { authService.register(signUpRequest) } throws IllegalArgumentException("Email already taken")
            then("결과: BadRequest status, error message") {
                try {
                    authController.register(signUpRequest)
                    fail("Exception should have been thrown")
                } catch (e: IllegalArgumentException) {
                    e.message shouldBe "Email already taken"
                }
            }
        }
    }
})