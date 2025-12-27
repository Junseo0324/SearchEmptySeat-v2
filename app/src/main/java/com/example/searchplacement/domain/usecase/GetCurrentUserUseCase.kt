package com.example.searchplacement.domain.usecase

import com.example.searchplacement.data.mapper.toModel
import com.example.searchplacement.domain.model.User
import com.example.searchplacement.domain.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend fun execute(): User? {
        return userRepository.getUser()?.toModel()
    }
}
