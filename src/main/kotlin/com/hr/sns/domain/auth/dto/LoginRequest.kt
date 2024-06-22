package com.hr.sns.domain.auth.dto

data class LoginRequest(
    val email:String,
    val pw:String
)