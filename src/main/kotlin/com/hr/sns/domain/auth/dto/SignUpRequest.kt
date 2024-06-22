package com.hr.sns.domain.auth.dto

data class SignUpRequest(
    val name: String,
    val email: String,
    val pw: String
)