package com.hr.todoapp.domain.auth.oauth.type

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class OAuthProviderConverter:Converter<String,OAuthProvider> {
    override fun convert(source: String): OAuthProvider {
        return runCatching {
            OAuthProvider.valueOf(source.uppercase())
        }.getOrElse {
            throw IllegalArgumentException("Provider not found")
        }
    }
}