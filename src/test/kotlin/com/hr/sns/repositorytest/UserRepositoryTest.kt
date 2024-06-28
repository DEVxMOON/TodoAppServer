package com.hr.sns.repositorytest

import com.hr.sns.domain.user.entity.User
import com.hr.sns.domain.user.repository.UserRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class UserRepositoryTest : BehaviorSpec({

    val userRepository: UserRepository = mockk()

    val user1 = User("user1", "user1@example.com", "pw1")

    given("UserRepository Test") {

        `when`("Checking if email exists") {
            val existingEmail = "user1@example.com"
            val nonExistingEmail = "no@example.com"
            every { userRepository.existsByEmail(existingEmail) } returns true
            every { userRepository.existsByEmail(nonExistingEmail) } returns false
            then("존재 유무 확인 결과") {
                userRepository.existsByEmail(existingEmail) shouldBe true
                userRepository.existsByEmail(nonExistingEmail) shouldBe false
            }
        }

        `when`("Finding user by email") {
            val existingEmail = "user1@example.com"
            val nonExistingEmail = "no@example.com"

            every { userRepository.findByEmail(existingEmail) } returns user1
            every { userRepository.findByEmail(nonExistingEmail) } returns null

            then("아이디 기준 확인 결과") {
                userRepository.findByEmail(existingEmail) shouldBe user1
                userRepository.findByEmail(nonExistingEmail) shouldBe null
            }
        }

        `when`("Finding user by provider name and provider id") {
            val providerName = "Naver"
            val providerId = "Naver1"
            val nonExistingProviderName = "Kakao"
            val nonExistingProviderId = "Kakao1"

            every { userRepository.findByProviderNameAndProviderId(providerName, providerId) } returns user1
            every { userRepository.findByProviderNameAndProviderId(nonExistingProviderName, providerId) } returns null
            every { userRepository.findByProviderNameAndProviderId(providerName, nonExistingProviderId) } returns null
            every { userRepository.findByProviderNameAndProviderId(nonExistingProviderName, nonExistingProviderId) } returns null

            then("provider name 과 id 기준 확인 결과"){
                userRepository.findByProviderNameAndProviderId(providerName, providerId) shouldBe user1
                userRepository.findByProviderNameAndProviderId(nonExistingProviderName, providerId) shouldBe null
                userRepository.findByProviderNameAndProviderId(providerName, nonExistingProviderId) shouldBe null
                userRepository.findByProviderNameAndProviderId(nonExistingProviderName, nonExistingProviderId) shouldBe null
            }
        }
    }
})