package com.hr.sns.domain.auth.oauth.client

import com.hr.sns.domain.auth.oauth.dto.OAuthLoginUserInfo
import com.hr.sns.domain.auth.oauth.type.OAuthProvider

interface OAuthClient {
    fun getLoginPageUrl():String
    fun getAccessToken(authorizationCode: String):String
    fun retrieveUserInfo(accessToken: String): OAuthLoginUserInfo
    fun supports(provider: OAuthProvider):Boolean
}