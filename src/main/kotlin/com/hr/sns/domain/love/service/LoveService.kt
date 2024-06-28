package com.hr.sns.domain.love.service

import com.hr.sns.domain.love.dto.LoveResponse
import com.hr.sns.domain.love.entity.Love
import com.hr.sns.domain.love.repository.LoveRepository
import com.hr.sns.domain.tweet.repository.TweetRepository
import com.hr.sns.exception.UnauthorizedActionException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class LoveService (
    private val tweetRepository: TweetRepository,
    private val loveRepository: LoveRepository
){

    fun addLove(tweetId:Long, userEmail:String): LoveResponse {
        val tweet = tweetRepository.findByIdOrNull(tweetId) ?: throw Exception("tweet not found")
        if(tweet.user.email == userEmail) throw UnauthorizedActionException("You can't make like with your tweet")

        val love= Love(
            email = userEmail,
            tweet = tweet,
        )
        loveRepository.save(love)
        return LoveResponse.from(love)
    }

    fun deleteLove(tweetId:Long, userEmail:String){
        val love = loveRepository.findByIdOrNull(tweetId) ?: throw Exception("tweet not found")
        if(love.email == userEmail) loveRepository.delete(love)
        else throw UnauthorizedActionException("Not your like")
    }

    fun getLoveWithUser(tweetId:Long):List<LoveResponse>{
        val loves = loveRepository.findByTweetId(tweetId)
        return LoveResponse.from(loves)
    }

    fun getLoveTweets(userEmail:String): List<LoveResponse>{
        val loves = loveRepository.findByEmail(userEmail)
        return LoveResponse.from(loves)
    }
}