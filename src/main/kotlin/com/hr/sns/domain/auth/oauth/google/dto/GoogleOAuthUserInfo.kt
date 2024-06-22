package com.hr.sns.domain.auth.oauth.google.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.hr.sns.domain.auth.oauth.dto.OAuthLoginUserInfo
import com.hr.sns.domain.auth.oauth.type.OAuthProvider

data class GoogleOAuthUserInfo(
    @JsonProperty("id") val providerId: String,
    @JsonProperty("name") val nickname: String,
    @JsonProperty("email") val googleEmail: String,
):OAuthLoginUserInfo(
    provider = OAuthProvider.GOOGLE,
    id = providerId,
    name = nickname,
    email = googleEmail
)