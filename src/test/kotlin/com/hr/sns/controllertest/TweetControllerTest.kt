package com.hr.sns.controllertest

import com.hr.sns.domain.tweet.controller.TweetController
import com.hr.sns.domain.tweet.dto.TweetRequest
import com.hr.sns.domain.tweet.dto.TweetResponse
import com.hr.sns.domain.tweet.service.TweetService
import com.hr.sns.infra.security.UserPrincipal
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.domain.PageImpl
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import java.time.LocalDateTime

class TweetControllerTest : BehaviorSpec({
    val tweetService = mockk<TweetService>()
    val tweetController = TweetController(tweetService)

    given("Tweet Controller Test") {
        val userPrincipal = mockk<UserPrincipal>()
        val authentication = mockk<Authentication>()
        every { authentication.principal } returns userPrincipal
        every { userPrincipal.id } returns 1L

        `when`("Create Tweet") {
            val tweetRequest = TweetRequest("tweet body", LocalDateTime.now(), null)
            val tweetResponse = TweetResponse(
                id = 1L,
                userName = "HR",
                tweet = "New Tweet",
                views = 0,
                createdAt = LocalDateTime.now(),
                tweetId = null
            )

            then("success") {
                every { tweetService.createTweet(tweetRequest, 1L) } returns tweetResponse
                val response = tweetController.createTweet(tweetRequest, authentication)
                response.statusCode shouldBe HttpStatus.CREATED
                response.body shouldBe tweetResponse
            }
            then("fail - invalid request"){
                every{ tweetService.createTweet(any(),any())} throws IllegalArgumentException("Invalid request")
                val response = tweetController.createTweet(TweetRequest("", LocalDateTime.now(),null), authentication)
                response.statusCode shouldBe HttpStatus.BAD_REQUEST
            }
        }

        `when`("Create Tweet Mention") {
            val tweetRequest = TweetRequest("tweet body", LocalDateTime.now(), 1L)
            val tweetResponse = TweetResponse(
                id = 1L,
                userName = "HR",
                tweet = "New Tweet",
                views = 0,
                createdAt = LocalDateTime.now(),
                tweetId = 2L
            )

            every { tweetService.createMention(2L,tweetRequest, 1L) } returns tweetResponse
            then("success") {
                val response = tweetController.createMention(2L,tweetRequest, authentication)
                response.statusCode shouldBe HttpStatus.CREATED
                response.body shouldBe tweetResponse
            }

            then("fail - tweetId not found"){
                every{ tweetService.createMention(any(),any(),any())} throws IllegalArgumentException("Invalid request")
                val response = tweetController.createMention(2L,tweetRequest, authentication)
                response.statusCode shouldBe HttpStatus.BAD_REQUEST
            }
        }

        `when`("Delete Tweet") {
            every { tweetService.deleteTweet(1L, 1L) } returns Unit

            then("success") {
                val response = tweetController.deleteTweet(1L, authentication)
                response.statusCode shouldBe HttpStatus.NO_CONTENT
                response.body shouldBe Unit
            }

            then("fail - tweet not found") {
                every { tweetService.deleteTweet(2L, 1L) } throws NoSuchElementException("Tweet not found")

                val response = tweetController.deleteTweet(2L, authentication)
                response.statusCode shouldBe HttpStatus.NOT_FOUND
            }
        }

        `when`("Home") {
            val tweetResponses = listOf(
                TweetResponse(
                    id = 1L,
                    userName = "HR",
                    tweet = "First Tweet",
                    views = 0,
                    createdAt = LocalDateTime.now(),
                    tweetId = null
                ),
                TweetResponse(
                    id = 2L,
                    userName = "HR",
                    tweet = "Second Tweet",
                    views = 0,
                    createdAt = LocalDateTime.now(),
                    tweetId = null
                )
            )

            every { tweetService.getTweets(0, 10) } returns tweetResponses

            then("success") {
                val response = tweetController.home(0, 10)
                response.statusCode shouldBe HttpStatus.OK
                response.body shouldBe tweetResponses
            }

            then("fail - invalid cursor") {
                every { tweetService.getTweets(any(), any()) } throws IllegalArgumentException("Invalid cursor")

                val response = tweetController.home(-1, 10)
                response.statusCode shouldBe HttpStatus.BAD_REQUEST
            }
        }

        `when`("Get Tweet By Id") {
            val tweetResponse = TweetResponse(
                id = 1L,
                userName = "HR",
                tweet = "tweet body",
                views = 0,
                createdAt = LocalDateTime.now(),
                tweetId = null
            )
            then("Success") {
                val id = 1L
                every { tweetService.getTweetById(id) } returns tweetResponse

                val response = tweetController.getTweetById(id)
                response.statusCode shouldBe HttpStatus.OK
                response.body shouldBe tweetResponse
            }
            then("Fail - not found") {
                val id = 2L
                every { tweetService.getTweetById(id) } returns tweetResponse

                val response = tweetController.getTweetById(id)
                response.statusCode shouldBe HttpStatus.OK
                response.body shouldBe tweetResponse
            }
        }

        `when`("Search Tweet") {
            val tweetResponses = PageImpl(
                listOf(
                    TweetResponse(
                        id = 1L,
                        userName = "HR",
                        tweet = "First Tweet",
                        views = 0,
                        createdAt = LocalDateTime.now(),
                        tweetId = null
                    ),
                    TweetResponse(
                        id = 2L,
                        userName = "HR",
                        tweet = "Second Tweet",
                        views = 0,
                        createdAt = LocalDateTime.now(),
                        tweetId = null
                    )
                )
            )

            every { tweetService.searchTweets("tweet", 0, 5) } returns tweetResponses

            then("success") {
                val response = tweetController.searchTweets("tweet", 0, 5)
                response shouldBe tweetResponses
            }

            then("fail - invalid search") {
                every { tweetService.searchTweets(any(), any(), any()) } throws IllegalArgumentException("Invalid search keyword")

                val response = tweetController.searchTweets("", 0, 5)
                response shouldBe PageImpl(emptyList())
            }
        }
    }
})