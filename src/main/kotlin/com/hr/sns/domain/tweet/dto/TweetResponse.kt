package com.hr.sns.domain.tweet.dto

import com.hr.sns.domain.tweet.entity.Tweet
import java.time.LocalDateTime

data class TweetResponse (
    val id : Long,
    val userName:String,
    val tweet:String,
    val views:Int,
    val createdAt: LocalDateTime,
    val tweetId:Long?,
){
    companion object{
        fun from(tweet: Tweet) = TweetResponse(
            id = tweet.id!!,
            userName = tweet.user.name,
            tweet = tweet.tweet,
            views = tweet.views,
            createdAt = tweet.createdAt,
            tweetId = tweet.tweetId,
        )
    }
}