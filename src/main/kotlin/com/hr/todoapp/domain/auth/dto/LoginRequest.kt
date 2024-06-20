package com.hr.todoapp.domain.auth.dto

data class LoginRequest(
    val email:String,
    val pw:String
)