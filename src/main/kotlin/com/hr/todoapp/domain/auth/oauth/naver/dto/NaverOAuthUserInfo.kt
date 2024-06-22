package com.hr.todoapp.domain.auth.oauth.naver.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.hr.todoapp.domain.auth.oauth.dto.OAuthLoginUserInfo
import com.hr.todoapp.domain.auth.oauth.type.OAuthProvider

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
