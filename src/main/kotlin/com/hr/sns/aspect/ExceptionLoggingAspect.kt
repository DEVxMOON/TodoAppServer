package com.hr.sns.aspect

import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class ExceptionLoggingAspect {
    private val logger = LoggerFactory.getLogger(ExceptionLoggingAspect::class.java)

    @AfterThrowing(pointcut = "execution(* com.hr.sns.domain..*(..))", throwing = "ex")
    fun logException(ex: RuntimeException) {
        logger.error("Exception caught : $ex.message")
    }
}