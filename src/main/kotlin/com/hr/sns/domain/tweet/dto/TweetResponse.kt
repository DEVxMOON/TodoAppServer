package com.hr.sns.domain.tweet.dto

import java.time.LocalDateTime

data class TweetResponse (
    val userName:String,
    val tweet:String,
    val views:Int,
    val createdAt: LocalDateTime,
    val tweetId:Long?,
)