package com.hr.sns.domain.user.controller

import com.hr.sns.domain.user.dto.PasswordChangeRequest
import com.hr.sns.domain.user.dto.UpdateUserRequest
import com.hr.sns.domain.user.dto.UserResponse
import com.hr.sns.domain.user.service.UserService
import org.springframework.http.HttpStatus
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

    @PutMapping("/{id}")
    fun updateUserById(@PathVariable id: Long, @RequestBody updateUserRequest: UpdateUserRequest): ResponseEntity<UserResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.updateUserById(id, updateUserRequest))
    }

    @PatchMapping("/{id}/password-change")
    fun changePassword(
        @RequestBody passwordChangeRequest: PasswordChangeRequest,
        @PathVariable id :Long,
    ):ResponseEntity<String>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.changePassword(passwordChangeRequest, id))
    }

    @DeleteMapping("/{id}")
    fun deleteUserById(@PathVariable id: Long): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(userService.delete(id))
    }
}