package com.hr.todoapp.domain.auth.dto

data class PasswordChangeRequest(
    val oldPw: String,
    val newPw: String
)