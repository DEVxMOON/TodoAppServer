package com.hr.sns.domain.tweet.controller

import com.hr.sns.domain.tweet.dto.TweetRequest
import com.hr.sns.domain.tweet.dto.TweetResponse
import io.swagger.v3.oas.annotations.parameters.RequestBody
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tweets")
class TweetController {
    //트윗 생성
    @PostMapping
    fun createTweet(@RequestBody tweetRequest : TweetRequest) : ResponseEntity<TweetResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build()
    }
    //트윗 삭제
    @DeleteMapping("/{id}")
    fun deleteTweet(@PathVariable id : Long) : ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build()
    }

    //트윗 전체보기
    @GetMapping("/home")
    fun home():ResponseEntity<List<TweetResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .build()
    }
    //트윗 상세보기
    @GetMapping("/{id}")
    fun getTweetById(@PathVariable id : Long) : ResponseEntity<TweetResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .build()
    }

    //트윗 검색하기

}