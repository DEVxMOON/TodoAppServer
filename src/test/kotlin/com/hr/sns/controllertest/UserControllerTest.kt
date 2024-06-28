package com.hr.sns.controllertest

import com.hr.sns.domain.user.controller.UserController
import com.hr.sns.domain.user.dto.PasswordChangeRequest
import com.hr.sns.domain.user.dto.UpdateUserRequest
import com.hr.sns.domain.user.dto.UserResponse
import com.hr.sns.domain.user.service.UserService
import com.hr.sns.infra.security.UserPrincipal
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication

class UserControllerTest : BehaviorSpec({
    val userService = mockk<UserService>()
    val userController = UserController(userService)

    given("User Controller Test") {
        `when`("getAllUser") {
            then("success") {
                val userList = listOf(
                    UserResponse(1L, "user1", "user1@test.com"),
                    UserResponse(2L, "user2", "user2@test.com")
                )
                every { userService.getUserList() } returns userList

                val response = userController.getAllUser()
                response.statusCode shouldBe HttpStatus.OK
                response.body shouldBe userList
                response.body?.size shouldBe 2
            }
            then("empty list") {
                every { userService.getUserList() } returns emptyList()

                val response = userController.getAllUser()
                response.statusCode shouldBe HttpStatus.OK
                response.body shouldBe emptyList()
            }
        }

        `when`("getUserById") {
            val userResponse = UserResponse(1L, "user1", "user1@test.com")
            then("success") {
                val userId = 1L
                every { userService.getUserById(userId) } returns userResponse

                val response = userController.getUserById(userId)
                response.statusCode shouldBe HttpStatus.OK
                response.body shouldBe userResponse
            }
            then("fail") {
                val userId = 2L
                every { userService.getUserById(userId) } returns userResponse

                val response = userController.getUserById(userId)
                response.statusCode shouldBe HttpStatus.OK
                response.body shouldBe userResponse
            }
        }

        `when`("updateUserById") {
            val authentication = mockk<Authentication>()
            val userPrincipal = UserPrincipal(1L, "user1@test.com", listOf())
            every { authentication.principal } returns userPrincipal

            then("success") {
                val updateUserRequest = UpdateUserRequest("HR")
                val updateUserResponse = UserResponse(1L, "HR", "user1@test.com")
                every { userService.updateUserById(userPrincipal.id, updateUserRequest) } returns updateUserResponse

                val response = userController.updateUserById(authentication, updateUserRequest)
                response.statusCode shouldBe HttpStatus.OK
                response.body shouldBe updateUserResponse
            }
            then("fail - invalid request") {
                val invalidUpdateUserRequest = UpdateUserRequest("")
                every {
                    userService.updateUserById(
                        userPrincipal.id,
                        invalidUpdateUserRequest
                    )
                } throws InvalidRequestException("Invalid request data")

                val response = userController.updateUserById(authentication, invalidUpdateUserRequest)
                response.statusCode shouldBe HttpStatus.BAD_REQUEST
                response.body shouldBe "Invalid request data"
            }

            then("fail - no authentication") {
                val updateUserRequest = UpdateUserRequest("HR")
                val unauthenticatedUserPrincipal = UserPrincipal(2L, "user2@test.com", listOf())
                val unauthenticatedAuthentication = mockk<Authentication>()
                every { unauthenticatedAuthentication.principal } returns unauthenticatedUserPrincipal

                every {
                    userService.updateUserById(
                        unauthenticatedUserPrincipal.id,
                        updateUserRequest
                    )
                } throws NoAuthenticationException("No authentication")

                val response = userController.updateUserById(unauthenticatedAuthentication, updateUserRequest)
                response.statusCode shouldBe HttpStatus.FORBIDDEN
                response.body shouldBe "No authentication"
            }
        }

        `when`("changePassword") {
            val authentication = mockk<Authentication>()
            val userPrincipal = UserPrincipal(1L, "user@test.com", listOf())
            every { authentication.principal } returns userPrincipal

            then("success") {
                val passwordChangeRequest = PasswordChangeRequest("password", "newPassword")
                every {
                    userService.changePassword(
                        passwordChangeRequest,
                        userPrincipal.id
                    )
                } returns "Password changed successfully"

                val response = userController.changePassword(passwordChangeRequest, authentication)
                response.statusCode shouldBe HttpStatus.OK
                response.body shouldBe "Password changed successfully"
            }

            then("fail - invalid password change request") {
                val invalidPasswordChangeRequest = PasswordChangeRequest("password", "")
                every {
                    userService.changePassword(
                        invalidPasswordChangeRequest,
                        userPrincipal.id
                    )
                } throws InvalidRequestException("Invalid password change request")

                val response = userController.changePassword(invalidPasswordChangeRequest, authentication)
                response.statusCode shouldBe HttpStatus.BAD_REQUEST
                response.body shouldBe "Invalid password change request"
            }

            then("fail - no authentication") {
                val unauthenticatedUserPrincipal = UserPrincipal(2L, "user2@test.com", listOf())
                val unauthenticatedAuthentication = mockk<Authentication>()
                every { unauthenticatedAuthentication.principal } returns unauthenticatedUserPrincipal

                val changePasswordRequest = PasswordChangeRequest("password", "newPassword")
                every {
                    userService.changePassword(
                        changePasswordRequest,
                        unauthenticatedUserPrincipal.id
                    )
                } throws NoAuthenticationException("No authentication")

                val response = userController.changePassword(changePasswordRequest, unauthenticatedAuthentication)
                response.statusCode shouldBe HttpStatus.FORBIDDEN
                response.body shouldBe "No authentication"
            }
        }

        `when`("deleteUserById") {
            then("success") {
                val authentication = mockk<Authentication>()
                val userPrincipal = UserPrincipal(1L, "user1@example.com", listOf())
                every { authentication.principal } returns userPrincipal

                every { userService.delete(userPrincipal.id) } returns "User deleted successfully"

                val response = userController.deleteUserById(authentication)

                response.statusCode shouldBe HttpStatus.NO_CONTENT
                response.body shouldBe "User deleted successfully"
            }

            then("fail - user not found") {
                val authentication = mockk<Authentication>()
                val userPrincipal = UserPrincipal(999L, "user999@example.com", listOf())
                every { authentication.principal } returns userPrincipal

                every { userService.delete(userPrincipal.id) } throws NoSuchElementException("User not found")

                val response = userController.deleteUserById(authentication)

                response.statusCode shouldBe HttpStatus.NOT_FOUND
                response.body shouldBe null
            }
        }
    }
})

class InvalidRequestException(message: String) : RuntimeException(message)
class NoAuthenticationException(message: String) : RuntimeException(message)