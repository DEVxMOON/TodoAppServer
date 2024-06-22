package com.hr.todoapp.domain.auth.oauth.dto

import com.hr.todoapp.domain.auth.oauth.type.OAuthProvider

open class OAuthLoginUserInfo(
    val provider: OAuthProvider,
    val id: String,
    val name: String,
    val email: String,
)