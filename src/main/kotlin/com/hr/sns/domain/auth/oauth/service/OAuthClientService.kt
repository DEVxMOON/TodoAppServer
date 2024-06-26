package com.hr.sns.domain.auth.oauth.service

import com.hr.sns.domain.auth.oauth.client.OAuthClient
import com.hr.sns.domain.auth.oauth.dto.OAuthLoginUserInfo
import com.hr.sns.domain.auth.oauth.type.OAuthProvider
import com.hr.sns.exception.OAuthProviderNotFoundException
import org.springframework.stereotype.Service

@Service
class OAuthClientService(
    private val clients: List<OAuthClient>,
) {
    fun getLoginPageUrl(provider: OAuthProvider): String {
        return this.selectClient(provider).getLoginPageUrl()
    }

    fun login(provider: OAuthProvider, code: String): OAuthLoginUserInfo {
        val client = this.selectClient(provider)
        return client.getAccessToken(code)
            .let{ client.retrieveUserInfo(it)}
    }

    private fun selectClient(provider: OAuthProvider): OAuthClient {
        return clients.find { it.supports(provider) }
            ?: throw OAuthProviderNotFoundException("OAuth provider not found")
    }
}