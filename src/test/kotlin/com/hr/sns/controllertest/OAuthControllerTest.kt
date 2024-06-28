package com.hr.sns.controllertest

import com.hr.sns.domain.auth.dto.TokenResponse
import com.hr.sns.domain.auth.oauth.controller.OauthController
import com.hr.sns.domain.auth.oauth.service.OAuthClientService
import com.hr.sns.domain.auth.oauth.service.OauthLoginService
import com.hr.sns.domain.auth.oauth.type.OAuthProvider
import com.hr.sns.domain.auth.oauth.type.OAuthProviderConverter
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.servlet.http.HttpServletResponse

class OAuthControllerTest:BehaviorSpec({
    val oAuthProviderConverter = mockk<OAuthProviderConverter>()
    val oAuthClientService = mockk<OAuthClientService>()
    val oAuthLoginService = mockk<OauthLoginService>()
    val oauthController = OauthController(oAuthProviderConverter, oAuthClientService, oAuthLoginService)

    given("OAuth Controller Test") {
        `when`("Redirect Login Page") {
            val response = mockk<HttpServletResponse>(relaxed = true)
            val provider = "google"
            val loginUrl = "https://login.url"
            val oAuthProvider = OAuthProvider.GOOGLE

            every { oAuthProviderConverter.convert(provider) } returns oAuthProvider
            every { oAuthClientService.getLoginPageUrl(oAuthProvider) } returns loginUrl

            then("success") {
                oauthController.redirectLoginPage(provider, response)
                verify { response.sendRedirect(loginUrl) }
            }

            then("fail - invalid provider") {
                every { oAuthProviderConverter.convert(provider) } throws IllegalArgumentException("Invalid provider")

                try {
                    oauthController.redirectLoginPage(provider, response)
                } catch (e: IllegalArgumentException) {
                    e.message shouldBe "Invalid provider"
                }
            }
        }

        `when`("Callback") {
            val provider = "naver"
            val code = "auth_code"
            val tokenResponse = TokenResponse(token = "access_token")
            val oAuthProvider = OAuthProvider.NAVER

            every { oAuthProviderConverter.convert(provider) } returns oAuthProvider
            every { oAuthLoginService.login(oAuthProvider, code) } returns tokenResponse

            then("success") {
                val response = oauthController.callback(provider, code)
                response shouldBe tokenResponse
            }

            then("fail - invalid code") {
                every { oAuthLoginService.login(oAuthProvider, code) } throws IllegalArgumentException("Invalid code")

                try {
                    oauthController.callback(provider, code)
                } catch (e: IllegalArgumentException) {
                    e.message shouldBe "Invalid code"
                }
            }
        }
    }
})