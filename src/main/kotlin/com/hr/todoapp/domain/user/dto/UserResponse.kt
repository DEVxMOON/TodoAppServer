package com.hr.todoapp.domain.user.dto

import com.hr.todoapp.domain.user.entity.User

data class UserResponse(
    val id:Long,
    val name:String,
    val email:String,
){
    companion object{
        fun from(user: User):UserResponse = UserResponse(
            id = user.id!!,
            name = user.name,
            email = user.email,
        )
        fun from(users: List<User>): List<UserResponse> {
            return users.map { from(it) }
        }
    }
}