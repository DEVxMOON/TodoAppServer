package com.hr.sns.servicetest

import com.hr.sns.domain.tweet.dto.TweetRequest
import com.hr.sns.domain.tweet.entity.Tweet
import com.hr.sns.domain.tweet.repository.TweetRepository
import com.hr.sns.domain.tweet.service.TweetService
import com.hr.sns.domain.user.entity.User
import com.hr.sns.domain.user.repository.UserRepository
import com.hr.sns.exception.TweetNotFoundException
import com.hr.sns.exception.UserNotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.persistence.EntityManager
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

class TweetServiceTest : BehaviorSpec({
    val tweetRepository = mockk<TweetRepository>()
    val userRepository = mockk<UserRepository>()
    val entityManager = mockk<EntityManager>()
    val tweetService = TweetService(tweetRepository, userRepository, entityManager)

    val user = User("testUser", "test@example.com", "password").apply { id = 1L }
    val tweetRequest = TweetRequest("This is a tweet", LocalDateTime.now(),null)
    val tweet = Tweet(user, tweetRequest.tweet, 0, tweetRequest.createdAt, null).apply { id = 1L }

    given("TweetService") {
        `when`("createTweet") {
            then("success") {
                every{ userRepository.findByIdOrNull(any())} returns user
                every { tweetRepository.save(any<Tweet>()) } returns tweet
                println(tweet)
                val response = tweetService.createTweet(tweetRequest, user.id!!)
                response.tweet shouldBe tweetRequest.tweet

                verify { userRepository.findByIdOrNull(any()) }
                verify { tweetRepository.save(any<Tweet>()) }
            }

            then("should fail if user not found") {
                every { userRepository.findByIdOrNull(user.id!!) } returns null

                val exception = shouldThrow<UserNotFoundException> {
                    tweetService.createTweet(tweetRequest, user.id!!)
                }
                exception.message shouldBe "User not found"

                verify { userRepository.findByIdOrNull(user.id!!) }
            }
        }

        `when`("creating a mention") {
            then("should create mention successfully") {
                every { userRepository.findByIdOrNull(user.id!!) } returns user
                every { tweetRepository.save(any<Tweet>()) } returns tweet

                val response = tweetService.createMention(tweet.id!!, tweetRequest, user.id!!)
                response.tweet shouldBe tweetRequest.tweet

                verify { userRepository.findByIdOrNull(user.id!!) }
                verify { tweetRepository.save(any<Tweet>()) }
            }

            then("should fail if user not found") {
                every { userRepository.findByIdOrNull(user.id!!) } returns null

                val exception = shouldThrow<UserNotFoundException> {
                    tweetService.createMention(tweet.id!!, tweetRequest, user.id!!)
                }
                exception.message shouldBe "User not found"

                verify { userRepository.findByIdOrNull(user.id!!) }
            }
        }

        `when`("deleting a tweet") {
            then("should delete tweet successfully") {
                every { tweetRepository.findByIdOrNull(tweet.id!!) } returns tweet
                every { tweetRepository.delete(any<Tweet>()) } returns Unit

                tweetService.deleteTweet(tweet.id!!, user.id!!)

                verify { tweetRepository.findByIdOrNull(tweet.id!!) }
                verify { tweetRepository.delete(any<Tweet>()) }
            }

            then("should fail if tweet not found") {
                every { tweetRepository.findByIdOrNull(tweet.id!!) } returns null

                val exception = shouldThrow<TweetNotFoundException> {
                    tweetService.deleteTweet(tweet.id!!, user.id!!)
                }
                exception.message shouldBe "Tweet not found"

                verify { tweetRepository.findByIdOrNull(tweet.id!!) }
            }

            then("should fail if user does not match") {
                val anotherUser = User("anotherUser", "another@example.com", "password").apply { id = 2L }
                every { tweetRepository.findByIdOrNull(tweet.id!!) } returns tweet

                val exception = shouldThrow<Exception> {
                    tweetService.deleteTweet(tweet.id!!, anotherUser.id!!)
                }
                exception.message shouldBe "User not match"

                verify { tweetRepository.findByIdOrNull(tweet.id!!) }
            }
        }

//        `when`("getting tweets with cursor pagination") {
//            then("should return list of TweetResponse") {
//                val tweets = listOf(tweet)
//                val queryFactory = mockk<JPAQueryFactory>()
//                every { entityManager.entityManagerFactory.createEntityManager() } returns entityManager
//                every { queryFactory.selectFrom(any()) } returns queryFactory
//                every { queryFactory.limit(any()) } returns queryFactory
//                every { queryFactory.orderBy(any()) } returns queryFactory
//                every { queryFactory.where(any()) } returns queryFactory
//                every { queryFactory.fetch() } returns tweets
//
//                val response = tweetService.getTweets(null, 10)
//                response.size shouldBe 1
//                response.first().tweet shouldBe tweet.tweet
//
//                verify { entityManager.entityManagerFactory.createEntityManager() }
//            }
//        }

        `when`("getting tweet by ID") {
            then("should return tweet response successfully") {
                every { tweetRepository.findByIdOrNull(tweet.id!!) } returns tweet

                val response = tweetService.getTweetById(tweet.id!!)
                response.tweet shouldBe tweet.tweet

                verify { tweetRepository.findByIdOrNull(tweet.id!!) }
            }

            then("should fail if tweet not found") {
                every { tweetRepository.findByIdOrNull(tweet.id!!) } returns null

                val exception = shouldThrow<TweetNotFoundException> {
                    tweetService.getTweetById(tweet.id!!)
                }
                exception.message shouldBe "Tweet not found"

                verify { tweetRepository.findByIdOrNull(tweet.id!!) }
            }
        }

//        `when`("searching tweets by keyword") {
//            then("should return paginated tweet responses") {
//                val tweets = listOf(tweet)
//                val pageable = PageRequest.of(0, 10)
//                val queryFactory = mockk<JPAQueryFactory>()
//                every { entityManager.entityManagerFactory.createEntityManager() } returns entityManager
//                every { queryFactory.selectFrom(any()) } returns queryFactory
//                every { queryFactory.where(any()) } returns queryFactory
//                every { queryFactory.offset(any()) } returns queryFactory
//                every { queryFactory.limit(any()) } returns queryFactory
//                every { queryFactory.fetch() } returns tweets
//                every { queryFactory.fetchCount() } returns 1L
//
//                val response = tweetService.searchTweets("keyword", 0, 10)
//                response.content.size shouldBe 1
//                response.content.first().tweet shouldBe tweet.tweet
//
//                verify { entityManager.getEntityManagerFactory().createEntityManager() }
//            }
//        }
    }
})
