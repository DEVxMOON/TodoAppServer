package com.hr.todoapp.domain.auth.oauth.service

import com.hr.todoapp.domain.auth.dto.TokenResponse
import com.hr.todoapp.domain.auth.oauth.type.OAuthProvider
import com.hr.todoapp.domain.auth.service.AuthService
import com.hr.todoapp.domain.user.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OauthLoginService(
    private val oAuthClientService: OAuthClientService,
    private val userService: UserService,
    private val authService: AuthService,
) {
    @Transactional
    fun login(provider: OAuthProvider, code: String): TokenResponse {
        return oAuthClientService.login(provider, code)
            .let { userService.registerIfAbsent(it) }
            .let { authService.generateToken(it) }
    }
}