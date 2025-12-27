package com.example.searchplacement.domain.usecase

import com.example.searchplacement.data.local.UserEntity
import com.example.searchplacement.domain.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun execute(): UserEntity? {
        return userRepository.getUser()
    }
}
