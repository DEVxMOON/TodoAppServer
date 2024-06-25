package com.hr.sns.domain.tweet.controller

import com.hr.sns.domain.tweet.dto.TweetRequest
import com.hr.sns.domain.tweet.dto.TweetResponse
import com.hr.sns.domain.tweet.service.TweetService
import com.hr.sns.infra.security.UserPrincipal
import io.swagger.v3.oas.annotations.parameters.RequestBody
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tweets")
class TweetController(private val tweetService: TweetService) {
    //트윗 생성
    @PostMapping
    fun createTweet(
        @RequestBody tweetRequest: TweetRequest,
        authentication: Authentication,
    ): ResponseEntity<TweetResponse> {
        val user = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(tweetService.createTweet(tweetRequest, user.id))
    }

    @PostMapping("/{id}")
    fun createMention(
        @PathVariable id: Long,
        @RequestBody tweetRequest: TweetRequest,
        authentication: Authentication,
    ): ResponseEntity<TweetResponse> {
        val user = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(tweetService.createMention(id, tweetRequest, user.id))
    }

    //트윗 삭제
    @DeleteMapping("/{id}")
    fun deleteTweet(
        @PathVariable id: Long,
        authentication: Authentication,
    ): ResponseEntity<Unit> {
        val user = authentication.principal as UserPrincipal
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(tweetService.deleteTweet(id, user.id))
    }

    //트윗 전체보기 - 페이지네이션 적용
    @GetMapping("/home")
    fun home(
        @RequestParam(defaultValue = "0") cursor: Long,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<List<TweetResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(tweetService.getTweets(cursor,size))
    }

    //트윗 상세보기
    @GetMapping("/{id}")
    fun getTweetById(@PathVariable id: Long): ResponseEntity<TweetResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(tweetService.getTweetById(id))
    }

    //트윗 검색하기
    @GetMapping("/search")
    fun searchTweets(
        @RequestParam keyword: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") size: Int
    ): Page<TweetResponse> {
        return tweetService.searchTweets(keyword, page, size)
    }
}