package com.hr.todoapp.domain.user.dto

data class PasswordChangeRequest(
    val oldPw: String,
    val newPw: String
)