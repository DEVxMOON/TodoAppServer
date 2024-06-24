package com.hr.sns.domain.tweet.service

import com.hr.sns.domain.tweet.dto.TweetRequest
import com.hr.sns.domain.tweet.dto.TweetResponse
import com.hr.sns.domain.tweet.entity.Tweet
import com.hr.sns.domain.tweet.repository.TweetRepository
import com.hr.sns.domain.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TweetService(
    private val tweetRepository: TweetRepository,
    private val userRepository: UserRepository
) {
    fun createTweet(tweetRequest: TweetRequest, userId: Long): TweetResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw Exception("User not found")
        val tweet = Tweet(
            user =user,
            tweet = tweetRequest.tweet,
            views = 0,
            createdAt = tweetRequest.createdAt,
            tweetId = null,
        )

        tweetRepository.save(tweet)
        return TweetResponse.from(tweet)
    }

    fun deleteTweet(id: Long, userId:Long) {
        val tweet = tweetRepository.findByIdOrNull(id)?: throw Exception("Tweet not found")
        if(tweet.user.id!= userId) throw Exception("User not match")
        tweetRepository.delete(tweet)
    }

    fun getTweets(): List<TweetResponse> {
        val tweets = tweetRepository.findAll()
        TODO()
    }

    fun getTweetById(id: Long): TweetResponse {
        val tweet = tweetRepository.findByIdOrNull(id)?: throw Exception("Tweet not found")
        return TweetResponse.from(tweet)
    }

}