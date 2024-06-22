package com.hr.sns.domain.user.repository

import com.hr.sns.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository :JpaRepository<User,Long>{
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): User?
    fun findByProviderNameAndProviderId(providerName: String, providerId: String): User?
}