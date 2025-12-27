package com.example.searchplacement.domain.usecase

import com.example.searchplacement.core.util.TokenManager
import com.example.searchplacement.domain.repository.UserRepository
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun execute() {
        // TODO: Implement actual API call to delete account
        userRepository.clearUserData()
        TokenManager.clearToken()
    }
}
