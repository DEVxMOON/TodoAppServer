package com.hr.sns.domain.love.entity

import jakarta.persistence.*

@Entity
@Table(name="love")
class Love(
    @Column(name="email", nullable = false)
    var email: String,

    @ManyToOne
    @JoinColumn(name="tweet_id", nullable = false)
    var tweet: Long,
){
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?=null
}