package com.hr.sns.domain.auth.oauth.naver.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.hr.sns.domain.auth.oauth.dto.OAuthLoginUserInfo
import com.hr.sns.domain.auth.oauth.type.OAuthProvider

data class NaverOAuthUserInfo(
    @JsonProperty("result code")
    val resultCode: String,
    val message: String,
    val response: NaverUserInfoProperties
) : OAuthLoginUserInfo(
    provider = OAuthProvider.NAVER,
    id = response.id,
    name = response.name,
    email = response.email
)
