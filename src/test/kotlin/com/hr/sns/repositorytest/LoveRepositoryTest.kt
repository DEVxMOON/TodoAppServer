package com.hr.sns.repositorytest

import com.hr.sns.domain.love.entity.Love
import com.hr.sns.domain.love.repository.LoveRepository
import com.hr.sns.domain.tweet.entity.Tweet
import com.hr.sns.domain.user.entity.User
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.LocalDateTime

@DataJpaTest
class LoveRepositoryTest :BehaviorSpec({
    val loveRepository: LoveRepository = mockk()

    val user = User("user1", "user1@example.com", "pw1")
    val tweet = Tweet(user = user, tweet = "tweet body", views = 1, createdAt = LocalDateTime.now(), tweetId = null)
    val love1 = Love("user@test.com", tweet)

    given("LoveRepository Test") {
        `when`("find loves by Tweet Id"){
            val tweetId = 1L
            val loveForTweet = listOf(love1)

            every { loveRepository.findByTweetId(tweetId) } returns loveForTweet

            then("success"){
                val result = loveRepository.findByTweetId(tweetId)
                result shouldBe loveForTweet
                result.size shouldBe 1
                result[0].email shouldBe love1.email
                result[0].tweet shouldBe tweet
            }
            then("fail - tweet not found"){
                val nonExistingTweetId = 999L
                every { loveRepository.findByTweetId(nonExistingTweetId) } returns emptyList()

                val result = loveRepository.findByTweetId(nonExistingTweetId)
                result shouldBe emptyList()
            }
        }
        `when`("find loves by Email") {
            val email = "user@test.com"
            val lovesByEmail = listOf(love1)

            every { loveRepository.findByEmail(email) } returns lovesByEmail

            then("it should return list of loves for the given email") {
                val result = loveRepository.findByEmail(email)
                result shouldBe lovesByEmail
                result.size shouldBe 1
                result[0].tweet shouldBe tweet
            }

            then("it should handle case where no loves found for the given email") {
                val nonExistingEmail = "nonexistent@test.com"
                every { loveRepository.findByEmail(nonExistingEmail) } returns emptyList()

                val result = loveRepository.findByEmail(nonExistingEmail)
                result shouldBe emptyList()
            }
        }

    }

})