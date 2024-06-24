package com.hr.sns.domain.tweet.repository

import com.hr.sns.domain.tweet.entity.Tweet
import org.springframework.data.jpa.repository.JpaRepository

interface TweetRepository : JpaRepository<Tweet, Long> {}