package com.hr.todoapp.domain.auth.oauth.controller

import com.hr.todoapp.domain.auth.oauth.service.OAuthClientService
import com.hr.todoapp.domain.auth.oauth.service.OauthLoginService
import com.hr.todoapp.domain.auth.oauth.type.OAuthProviderConverter
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/oauth")
class OauthController(
    private val oAuthProviderConverter : OAuthProviderConverter,
    private val oAuthClientService: OAuthClientService,
    private val oAuthLoginService: OauthLoginService
) {

    @GetMapping("/{provider}")
    fun redirectLoginPage(
        @PathVariable provider: String,
        response: HttpServletResponse
    ){
        val oAuthProvider = oAuthProviderConverter.convert(provider)
        oAuthClientService.getLoginPageUrl(oAuthProvider)
            .let{ response.sendRedirect(it)}
    }

    @GetMapping("/{provider}/callback")
    fun callback(
        @PathVariable provider: String,
        @RequestParam code: String,
    ):String{
        val oAuthProvider = oAuthProviderConverter.convert(provider)
        return oAuthLoginService.login(oAuthProvider, code)
    }
}