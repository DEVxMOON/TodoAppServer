package com.hr.todoapp.domain.auth.dto

data class PasswordChangeRequest(
    val oldPassword: String,
    val newPassword: String
)