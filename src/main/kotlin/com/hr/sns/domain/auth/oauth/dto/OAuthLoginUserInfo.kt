package com.hr.sns.domain.auth.oauth.dto

import com.hr.sns.domain.auth.oauth.type.OAuthProvider

open class OAuthLoginUserInfo(
    val provider: OAuthProvider,
    val id: String,
    val name: String,
    val email: String,
)