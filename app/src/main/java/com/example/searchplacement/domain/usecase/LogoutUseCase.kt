package com.example.searchplacement.domain.usecase

import com.example.searchplacement.core.util.TokenManager
import com.example.searchplacement.domain.repository.UserRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun execute() {
        userRepository.clearUserData()
        TokenManager.clearToken()
    }
}
