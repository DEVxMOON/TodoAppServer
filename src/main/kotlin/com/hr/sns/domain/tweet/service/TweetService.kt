package com.hr.sns.domain.tweet.service

import com.hr.sns.domain.tweet.dto.TweetRequest
import com.hr.sns.domain.tweet.dto.TweetResponse
import com.hr.sns.domain.tweet.repository.TweetRepository
import org.springframework.stereotype.Service

@Service
class TweetService (
    private val tweetRepository: TweetRepository
){
    fun createTweet(tweetRequest: TweetRequest):TweetResponse{
        TODO()
    }

    fun deleteTweet(tweetRequest: TweetRequest){
        TODO()
    }

    fun getTweets():List<TweetResponse>{
        TODO()
    }

    fun getTweetById(id:Long):TweetResponse{
        TODO()
    }

}