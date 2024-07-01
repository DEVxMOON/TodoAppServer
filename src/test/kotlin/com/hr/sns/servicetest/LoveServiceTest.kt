package com.hr.sns.servicetest

import com.hr.sns.domain.love.entity.Love
import com.hr.sns.domain.love.repository.LoveRepository
import com.hr.sns.domain.love.service.LoveService
import com.hr.sns.domain.tweet.entity.Tweet
import com.hr.sns.domain.tweet.repository.TweetRepository
import com.hr.sns.domain.user.entity.User
import com.hr.sns.exception.UnauthorizedActionException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

class LoveServiceTest : BehaviorSpec({
    val tweetRepository = mockk<TweetRepository>()
    val loveRepository = mockk<LoveRepository>()

    val user1 = User("user1", "user1@test.com", "userPassword").apply { id = 1L }
    val user2 = User("user2", "user2@test.com", "userPassword").apply { id = 2L }
    val tweet = Tweet(user1, "tweet", 0, LocalDateTime.now(), null).apply { id = 1L }
    val love = Love(user2.email, tweet).apply { id = 1L }

    val loveService = LoveService(tweetRepository, loveRepository)

    given("LoveService") {

        `when`("adding love to a tweet") {
            then("success") {

                every { tweetRepository.findByIdOrNull(tweet.id) } returns tweet
                every { loveRepository.save(any<Love>()) } returns love

                val result = loveService.addLove(love.tweet.id!!, love.email)

                result.email shouldBe love.email
                result.tweetId shouldBe love.tweet.id

                verify { loveRepository.save(any<Love>()) }

            }
            then("fail - no authentication") {
                shouldThrow<UnauthorizedActionException> {
                    loveService.addLove(tweet.id!!, user2.email)
                }
            }

            then("fail - tweet not found") {
                every { tweetRepository.findByIdOrNull(tweet.id) } returns null
                shouldThrow<Exception> {
                    loveService.addLove(tweet.id!!, user2.email)
                }.message shouldBe "tweet not found"
            }
        }

        `when`("deleting love from a tweet") {
            every { loveRepository.findByIdOrNull(tweet.id!!) } returns love
            every { loveRepository.delete(any()) } returns Unit

            then("should delete love successfully if the user owns the love") {
                loveService.deleteLove(tweet.id!!, user2.email)
            }

            then("should not delete love if the user does not own the love") {
                shouldThrow<UnauthorizedActionException> {
                    loveService.deleteLove(tweet.id!!, "anotherUser@test.com")
                }
            }

            then("should throw exception if love not found") {
                every { loveRepository.findByIdOrNull(tweet.id!!) } returns null
                shouldThrow<Exception> {
                    loveService.deleteLove(tweet.id!!, user2.email)
                }.message shouldBe "tweet not found"
            }
        }

        `when`("getting loves for a tweet") {
            val loves = listOf(love)
            every { loveRepository.findByTweetId(tweet.id!!) } returns loves

            then("should return list of LoveResponse") {
                val responses = loveService.getLoveWithUser(tweet.id!!)
                responses.size shouldBe 1
                responses.first().email shouldBe love.email
                responses.first().tweetId shouldBe tweet.id
            }

            then("should return empty list if no loves found") {
                every { loveRepository.findByTweetId(tweet.id!!) } returns emptyList()
                val responses = loveService.getLoveWithUser(tweet.id!!)
                responses.size shouldBe 0
            }
        }

        `when`("getting love tweets for a user") {
            val loves = listOf(love)
            every { loveRepository.findByEmail(user1.email) } returns loves

            then("should return list of LoveResponse for user") {
                val responses = loveService.getLoveTweets(user1.email)
                responses.size shouldBe 1
                responses.first().email shouldBe love.email
                responses.first().tweetId shouldBe tweet.id
            }

            then("should return empty list if no love tweets found for user") {
                every { loveRepository.findByEmail(user1.email) } returns emptyList()
                val responses = loveService.getLoveTweets(user1.email)
                responses.size shouldBe 0
            }
        }
    }
})
