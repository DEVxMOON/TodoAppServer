package com.hr.sns.domain.tweet.repository

import com.hr.sns.domain.tweet.entity.Tweet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface TweetRepository : JpaRepository<Tweet, Long>, QuerydslPredicateExecutor<Tweet> {}