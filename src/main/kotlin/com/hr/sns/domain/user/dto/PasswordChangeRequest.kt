package com.hr.sns.domain.user.dto

data class PasswordChangeRequest(
    val oldPw: String,
    val newPw: String
)