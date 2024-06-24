package com.hr.sns.domain.tweet.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tweets")
class Tweet (
    @Column(name = "user_id",nullable = false)
    val userId: String,

    @Column(name = "tweet", nullable = false)
    var tweet: String,

    @Column(name = "views",nullable = false)
    var views: Int = 0,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name="tweet_id")
    var tweetId: Long?
){
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   var id: Long? = null
}