package com.hr.todoapp.domain.auth.dto

data class SignUpRequest(
    val name: String,
    val email: String,
    val pw: String
)