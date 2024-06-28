package com.hr.sns.controllertest

import com.hr.sns.domain.love.controller.LoveController
import com.hr.sns.domain.love.dto.LoveResponse
import com.hr.sns.domain.love.service.LoveService
import com.hr.sns.infra.security.UserPrincipal
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication

class LoveControllerTest : BehaviorSpec({
    val loveService = mockk<LoveService>()
    val loveController = LoveController(loveService)

    given("Love Controller Test") {
        val authentication = mockk<Authentication>()
        val userPrincipal = UserPrincipal(id = 1L, email = "test@example.com", listOf())

        every { authentication.principal } returns userPrincipal

        `when`("Add Love") {
            val tweetId = 1L
            val loveResponse = LoveResponse(id = 1L, email = "test@example.com", tweetId = tweetId)

            every { loveService.addLove(tweetId, userPrincipal.email) } returns loveResponse

            then("success") {
                val response = loveController.addLove(tweetId, authentication)
                response.statusCode shouldBe HttpStatus.CREATED
                response.body shouldBe loveResponse
            }

            then("fail - service throws exception") {
                every {
                    loveService.addLove(
                        tweetId,
                        userPrincipal.email
                    )
                } throws IllegalStateException("Failed to add love")

                try {
                    loveController.addLove(tweetId, authentication)
                } catch (e: IllegalStateException) {
                    e.message shouldBe "Failed to add love"
                }
            }
        }

        `when`("Remove Love") {
            val tweetId = 1L

            every { loveService.deleteLove(tweetId, userPrincipal.email) } returns Unit

            then("success") {
                val response = loveController.removeLove(tweetId, authentication)
                response.statusCode shouldBe HttpStatus.NO_CONTENT
            }

            then("fail - service throws exception") {
                every {
                    loveService.deleteLove(
                        tweetId,
                        userPrincipal.email
                    )
                } throws IllegalStateException("Failed to remove love")

                try {
                    loveController.removeLove(tweetId, authentication)
                } catch (e: IllegalStateException) {
                    e.message shouldBe "Failed to remove love"
                }
            }
        }

        `when`("Get Love With User") {
            val tweetId = 1L
            val loveResponses = listOf(LoveResponse(id = 1L, email = "test@example.com", tweetId = tweetId))

            every { loveService.getLoveWithUser(tweetId) } returns loveResponses

            then("success") {
                val response = loveController.getLoveWithUser(tweetId)
                response.statusCode shouldBe HttpStatus.OK
                response.body shouldBe loveResponses
            }

            then("fail - service throws exception") {
                every { loveService.getLoveWithUser(tweetId) } throws IllegalStateException("Failed to get loves")

                try {
                    loveController.getLoveWithUser(tweetId)
                } catch (e: IllegalStateException) {
                    e.message shouldBe "Failed to get loves"
                }
            }
        }

        `when`("Get Users Loved Tweets") {
            val loveResponses = listOf(LoveResponse(id = 1L, email = "test@example.com", tweetId = 1L))

            every { loveService.getLoveTweets(userPrincipal.email) } returns loveResponses

            then("success") {
                val response = loveController.getUsersLovedTweets(authentication)
                response.statusCode shouldBe HttpStatus.OK
                response.body shouldBe loveResponses
            }

            then("fail - service throws exception") {
                every { loveService.getLoveTweets(userPrincipal.email) } throws IllegalStateException("Failed to get user's loves")

                try {
                    loveController.getUsersLovedTweets(authentication)
                } catch (e: IllegalStateException) {
                    e.message shouldBe "Failed to get user's loves"
                }
            }
        }
    }
})