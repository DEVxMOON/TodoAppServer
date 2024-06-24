package com.hr.sns.domain.love.repository

import com.hr.sns.domain.love.entity.Love
import org.springframework.data.jpa.repository.JpaRepository

interface LoveRepository: JpaRepository<Love, Long>{
    fun findByTweetId(tweetId: Long): List<Love>
    fun findByUserId(userId: Long): List<Love>
}