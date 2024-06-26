package com.hr.sns.domain.user.service

import com.hr.sns.domain.auth.oauth.dto.OAuthLoginUserInfo
import com.hr.sns.domain.user.dto.PasswordChangeRequest
import com.hr.sns.domain.user.dto.UpdateUserRequest
import com.hr.sns.domain.user.dto.UserResponse
import com.hr.sns.domain.user.entity.User
import com.hr.sns.domain.user.repository.UserRepository
import com.hr.sns.exception.InvalidPasswordException
import com.hr.sns.exception.UserNotFoundException
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
        val user = userRepository.findByIdOrNull(id) ?: throw UserNotFoundException("User not found")
        return UserResponse.from(user)
    }

    fun updateUserById(id: Long, request: UpdateUserRequest): UserResponse {
        val user = userRepository.findByIdOrNull(id) ?: throw UserNotFoundException("User not found")
        user.name = request.name
        //변경할 사항들 생길시 이곳에 추가
        return UserResponse.from(user)
    }

    fun changePassword(request: PasswordChangeRequest, userId: Long): String {
        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException("User with id $userId not found.")
        val newPasswordEncoder = passwordEncoder.encode(request.newPw)
        if (!passwordEncoder.matches(request.oldPw, user.pw)) {
            throw InvalidPasswordException("User with id ${user.id} does not match password.")
        }
        if (passwordEncoder.matches(request.newPw, request.oldPw)) {
            throw InvalidPasswordException("Password does not changed")
        }

        user.pw = newPasswordEncoder

        return "Password Changed Successfully!"
    }

    fun delete(id: Long): String {
        val user = userRepository.findByIdOrNull(id) ?: throw UserNotFoundException("User not found.")
        userRepository.delete(user)
        return "Deleted Successfully!"
    }

    fun registerIfAbsent(userInfo: OAuthLoginUserInfo): User {
        return userRepository.findByProviderNameAndProviderId(userInfo.provider.name, userInfo.id)
            ?: run {
                val user = User(
                    name = userInfo.name,
                    email = userInfo.email,
                    pw = "",
                    providerName = userInfo.provider.name,
                    providerId = userInfo.id
                )
                userRepository.save(user)
            }
    }
}