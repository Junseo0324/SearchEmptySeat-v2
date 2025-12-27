package com.example.searchplacement.domain.usecase

import com.example.searchplacement.domain.repository.UserRepository
import com.example.searchplacement.data.local.UserEntity
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun execute(): UserEntity? {
        return userRepository.getUser()
    }
}
