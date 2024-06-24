package com.hr.sns.domain.love.dto

import com.hr.sns.domain.love.entity.Love

data class LoveResponse (
    val id:Long,
    val email :String,
    val feedId:Long
){
    companion object{
        fun from(love : Love) = LoveResponse(
            email=love.email,
            feedId = love.tweet.tweetId!!,
            id = love.id!!
        )
        fun from(loves: List<Love>) = loves.map { love ->
            LoveResponse(
                email = love.email,
                feedId = love.tweet.tweetId!!,
                id = love.id!!
            )
        }
    }
}