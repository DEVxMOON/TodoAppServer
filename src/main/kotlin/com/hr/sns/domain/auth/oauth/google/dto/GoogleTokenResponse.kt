package com.hr.sns.domain.auth.oauth.google.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
data class GoogleTokenResponse (
    val accessToken: String
)