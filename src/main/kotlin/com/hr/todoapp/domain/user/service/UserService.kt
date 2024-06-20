package com.hr.todoapp.domain.user.service

import com.hr.todoapp.domain.user.dto.PasswordChangeRequest
import com.hr.todoapp.domain.user.dto.UpdateUserRequest
import com.hr.todoapp.domain.user.dto.UserResponse
import com.hr.todoapp.domain.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun getUserList(): List<UserResponse> {
        val userList = userRepository.findAll()
        return UserResponse.from(userList)
    }

    fun getUserById(id: Long): UserResponse {
        val user = userRepository.findByIdOrNull(id)?: throw Exception("User not found")
        return UserResponse.from(user)
    }

    fun updateUserById(id: Long, request: UpdateUserRequest): UserResponse {
        val user = userRepository.findByIdOrNull(id)?: throw Exception("User not found")
        user.name = request.name
        //변경할 사항들 생길시 이곳에 추가
        return UserResponse.from(user)
    }

    fun changePassword(request: PasswordChangeRequest, userId:Long): String {
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

    fun delete(id:Long):String{
        val user = userRepository.findByIdOrNull(id) ?: throw Exception("User not found.")
        userRepository.delete(user)
        return "Deleted Successfully!"
    }
}