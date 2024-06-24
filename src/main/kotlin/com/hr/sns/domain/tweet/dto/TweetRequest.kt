package com.hr.sns.domain.tweet.dto

import java.time.LocalDateTime

data class TweetRequest (
    val tweet:String,
    val createdAt: LocalDateTime,
    val tweetId:Long?,
)