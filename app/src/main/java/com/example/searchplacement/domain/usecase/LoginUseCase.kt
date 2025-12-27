package com.example.searchplacement.domain.usecase

import com.example.searchplacement.core.util.Result
import com.example.searchplacement.core.util.TokenManager
import com.example.searchplacement.data.dto.login.LoginRequest
import com.example.searchplacement.data.mapper.toModel
import com.example.searchplacement.data.mapper.toUserEntity
import com.example.searchplacement.domain.model.LoginModel
import com.example.searchplacement.domain.repository.AuthRepository
import com.example.searchplacement.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend fun execute(
        email: String,
        password: String
    ): Result<LoginModel, String> {
        val response = authRepository.login(
            LoginRequest(email, password)
        )

        if (response.status != "success" || response.data == null) {
            return Result.Error(
                response.message
            )
        }

        val loginResponse = response.data

        // local 저장 + 토큰
        val userEntity = loginResponse.toUserEntity()
        userRepository.saveUser(userEntity)
        TokenManager.setToken(userEntity.token)

        return Result.Success(
            loginResponse.toModel()
        )
    }
}