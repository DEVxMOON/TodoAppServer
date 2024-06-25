package com.hr.sns.domain.tweet.service

import com.hr.sns.domain.tweet.dto.TweetRequest
import com.hr.sns.domain.tweet.dto.TweetResponse
import com.hr.sns.domain.tweet.entity.QTweet
import com.hr.sns.domain.tweet.entity.Tweet
import com.hr.sns.domain.tweet.repository.TweetRepository
import com.hr.sns.domain.user.repository.UserRepository
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TweetService(
    private val tweetRepository: TweetRepository,
    private val userRepository: UserRepository,
    private val entityManager: EntityManager,
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

    fun createMention(id:Long,tweetRequest: TweetRequest,userId: Long): TweetResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw Exception("User not found")
        val tweet = Tweet(
            user =user,
            tweet = tweetRequest.tweet,
            views = 0,
            createdAt = tweetRequest.createdAt,
            tweetId = id,
        )
        tweetRepository.save(tweet)
        return TweetResponse.from(tweet)
    }

    fun deleteTweet(id: Long, userId:Long) {
        val tweet = tweetRepository.findByIdOrNull(id)?: throw Exception("Tweet not found")
        if(tweet.user.id!= userId) throw Exception("User not match")
        tweetRepository.delete(tweet)
    }

    fun getTweets(cursor:Long?, size:Int): List<TweetResponse> {
        val queryFactory = JPAQueryFactory(entityManager)
        val tweet = QTweet.tweet1

        val query = queryFactory.selectFrom(tweet)
            .limit(size.toLong())
            .orderBy(tweet.createdAt.desc())

        cursor?.let{
            query.where(tweet.id.lt(it))
        }

        val tweets = query.fetch()
        return tweets.map { TweetResponse.from(it) }
    }

    fun getTweetById(id: Long): TweetResponse {
        val tweet = tweetRepository.findByIdOrNull(id)?: throw Exception("Tweet not found")
        return TweetResponse.from(tweet)
    }

    fun searchTweets(keyword:String, page:Int, size:Int): Page<TweetResponse> {
        val queryFactory = JPAQueryFactory(entityManager)
        val tweet = QTweet.tweet1

        val pageable: Pageable = PageRequest.of(page, size)

        val predicate = tweet.tweet.containsIgnoreCase(keyword)
            .or(tweet.user().name.containsIgnoreCase(keyword))

        val query = queryFactory.selectFrom(tweet)
            .where(predicate)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        val tweets = query.fetch()

        val tweetResponses = tweets.map { TweetResponse.from(it) }

        val totalQuery = queryFactory.selectFrom(tweet).where(predicate)
        val total = totalQuery.fetch().size.toLong()

        return PageImpl(tweetResponses, pageable, total)
    }

}