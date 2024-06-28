package com.hr.sns.domain.love.controller

import com.hr.sns.domain.love.dto.LoveResponse
import com.hr.sns.domain.love.service.LoveService
import com.hr.sns.infra.security.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/love")
class LoveController(
    private val loveService: LoveService
) {
    @PostMapping
    fun addLove(
        tweetId: Long,
        authentication: Authentication,
    ): ResponseEntity<LoveResponse> {
        val user = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(loveService.addLove(tweetId, user.email))
    }

    @DeleteMapping
    fun removeLove(
        tweetId: Long,
        authentication: Authentication
    ):ResponseEntity<Unit>{
        val user = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(loveService.deleteLove(tweetId,user.email))
    }

    @GetMapping("/tweet/{id}/loves")
    fun getLoveWithUser(
        @PathVariable id: Long,
    ):ResponseEntity<List<LoveResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(loveService.getLoveWithUser(id))
    }

    //사용자 본인의 좋아요 목록만 확인가능
    @GetMapping("/user/loves")
    fun getUsersLovedTweets(
        authentication: Authentication,
    ):ResponseEntity<List<LoveResponse>> {
        val user = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(loveService.getLoveTweets(user.email))
    }
}