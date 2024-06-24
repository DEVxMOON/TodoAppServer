package com.hr.sns.domain.love.entity

import com.hr.sns.domain.tweet.entity.Tweet
import com.hr.sns.domain.user.entity.User
import jakarta.persistence.*

@Entity
@Table(name="love")
class Love (
    @Column(name="email", nullable = false)
    var email: String,

    @ManyToOne
    @JoinColumn(name="tweet_id", nullable = false)
    var tweet:Tweet,
){
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?=null
}