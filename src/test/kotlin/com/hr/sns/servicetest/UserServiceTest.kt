package com.hr.sns.servicetest

import com.hr.sns.domain.auth.oauth.dto.OAuthLoginUserInfo
import com.hr.sns.domain.auth.oauth.type.OAuthProvider
import com.hr.sns.domain.user.dto.PasswordChangeRequest
import com.hr.sns.domain.user.dto.UpdateUserRequest
import com.hr.sns.domain.user.dto.UserResponse
import com.hr.sns.domain.user.entity.User
import com.hr.sns.domain.user.repository.UserRepository
import com.hr.sns.domain.user.service.UserService
import com.hr.sns.exception.InvalidPasswordException
import com.hr.sns.exception.UserNotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder

class UserServiceTest : BehaviorSpec({
    val userRepository = mockk<UserRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val userService = UserService(userRepository, passwordEncoder)

    given("UserServiceTest") {

        `when`("getUserList") {
            then("success") {
                val users = listOf(User("user1", "user1@test.com", "password",null,null).apply{id=1L})
                every { userRepository.findAll() } returns users

                val result = userService.getUserList()
                result shouldBe UserResponse.from(users)
            }
        }

        `when`("getUserById") {
            then("success") {
                val users = listOf(User("user1", "user1@test.com", "password").apply{id=1L})
                val user = users[0]
                every { userRepository.findByIdOrNull(1L) } returns user

                val result = userService.getUserById(1L)
                result shouldBe UserResponse.from(user)
            }
            then("fail - user not found") {
                every { userRepository.findByIdOrNull(1L) } returns null

                shouldThrow<UserNotFoundException> {
                    userService.getUserById(1L)
                }
            }
        }

        `when`("updateUserById") {
            then("success") {
                val user = User( "user1", "user1@test.com", "password").apply {id=1L}
                val updateUserRequest = UpdateUserRequest("newName")
                every { userRepository.findByIdOrNull(1L) } returns user

                val result = userService.updateUserById(1L, updateUserRequest)
                result shouldBe UserResponse.from(user)
                user.name shouldBe "newName"
            }
            then("fail - user not found") {
                every { userRepository.findByIdOrNull(1L) } returns null
                val updateUserRequest = UpdateUserRequest("newName")

                shouldThrow<UserNotFoundException> {
                    userService.updateUserById(1L, updateUserRequest)
                }
            }
        }

        `when`("changePassword") {
            val user = User("user1", "user1@test.com", "encodedPassword").apply {id=1L}
            every { passwordEncoder.matches("oldPassword", "encodedPassword") } returns true
            every { passwordEncoder.matches("newPassword", "oldPassword") } returns false
            every { passwordEncoder.encode("newPassword") } returns "newEncodedPassword"

            then("success") {
                val passwordChangeRequest = PasswordChangeRequest("oldPassword", "newPassword")
                every { userRepository.findByIdOrNull(1L) } returns user

                val result = userService.changePassword(passwordChangeRequest, 1L)
                result shouldBe "Password Changed Successfully!"
                user.pw shouldBe "newEncodedPassword"
            }

            then("fail - user not found") {
                val passwordChangeRequest = PasswordChangeRequest("oldPassword", "newPassword")
                every { userRepository.findByIdOrNull(1L) } returns null

                shouldThrow<UserNotFoundException> {
                    userService.changePassword(passwordChangeRequest, 1L)
                }
            }

            then("fail - invalid old password") {
                val passwordChangeRequest = PasswordChangeRequest("wrongOldPassword", "newPassword")
                every { userRepository.findByIdOrNull(1L) } returns user
                every { passwordEncoder.matches("wrongOldPassword", "encodedPassword") } returns false

                shouldThrow<InvalidPasswordException> {
                    userService.changePassword(passwordChangeRequest, 1L)
                }
            }

            then("fail - new password matches old password") {
                val passwordChangeRequest = PasswordChangeRequest("oldPassword", "oldPassword")
                every { userRepository.findByIdOrNull(1L) } returns user
                every { passwordEncoder.matches("oldPassword", "encodedPassword") } returns true
                every { passwordEncoder.matches("oldPassword", "oldPassword") } returns true

                shouldThrow<InvalidPasswordException> {
                    userService.changePassword(passwordChangeRequest, 1L)
                }
            }
        }

        `when`("delete") {
            then("success") {
                val user = User("user1", "user1@test.com", "password").apply { id=1L }
                every { userRepository.findByIdOrNull(1L) } returns user
                every { userRepository.delete(user) } returns Unit

                val result = userService.delete(1L)
                result shouldBe "Deleted Successfully!"
            }
            then("fail - user not found") {
                every { userRepository.findByIdOrNull(1L) } returns null

                shouldThrow<UserNotFoundException> {
                    userService.delete(1L)
                }
            }
        }

        `when`("registerIfAbsent") {
            then("success - user not found, register new user") {
                val userInfo = OAuthLoginUserInfo(OAuthProvider.NAVER, "id", "name", "email")
                every { userRepository.findByProviderNameAndProviderId("provider", "id") } returns null
                val newUser =
                    User(name = "name", email = "email", pw = "", providerName = "provider", providerId = "id")
                every { userRepository.save(any()) } returns newUser

                val result = userService.registerIfAbsent(userInfo)
                result shouldBe newUser
            }
            then("success - user found") {
                val userInfo = OAuthLoginUserInfo(OAuthProvider.NAVER, "id", "name", "email")
                val existingUser =
                    User(name = "name", email = "email", pw = "", providerName = "provider", providerId = "id")
                every { userRepository.findByProviderNameAndProviderId("provider", "id") } returns existingUser

                val result = userService.registerIfAbsent(userInfo)
                result shouldBe existingUser
            }
        }
    }
})