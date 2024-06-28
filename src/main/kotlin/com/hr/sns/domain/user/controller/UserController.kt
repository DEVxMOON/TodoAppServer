package com.hr.sns.domain.user.controller

import com.hr.sns.domain.user.dto.PasswordChangeRequest
import com.hr.sns.domain.user.dto.UpdateUserRequest
import com.hr.sns.domain.user.dto.UserResponse
import com.hr.sns.domain.user.service.UserService
import com.hr.sns.infra.security.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(private val userService: UserService) {

    @GetMapping("/all")
    fun getAllUser(): ResponseEntity<List<UserResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.getUserList())
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.getUserById(id))
    }

    @PutMapping
    fun updateUserById(authentication:Authentication, @RequestBody updateUserRequest: UpdateUserRequest): ResponseEntity<UserResponse> {
        val user = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.updateUserById(user.id, updateUserRequest))
    }

    @PatchMapping("/password-change")
    fun changePassword(
        @RequestBody passwordChangeRequest: PasswordChangeRequest,
        authentication:Authentication,
    ):ResponseEntity<String>{
        val user = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.changePassword(passwordChangeRequest, user.id))
    }

    @DeleteMapping
    fun deleteUserById(authentication:Authentication): ResponseEntity<String> {
        val user = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(userService.delete(user.id))
    }
}