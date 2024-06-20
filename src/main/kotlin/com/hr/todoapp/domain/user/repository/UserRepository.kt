package com.hr.todoapp.domain.user.repository

import com.hr.todoapp.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository :JpaRepository<User,Long>{
}