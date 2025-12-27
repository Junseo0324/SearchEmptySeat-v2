package com.example.searchplacement.domain.usecase

import com.example.searchplacement.core.util.Result
import com.example.searchplacement.data.dto.login.LoginRequest
import com.example.searchplacement.domain.repository.AuthRepository
import javax.inject.Inject

class CheckPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun execute(email: String, password: String): Result<Unit, String> {
        return try {
            val response = authRepository.login(LoginRequest(email, password))
            if (response.status == "success") {
                Result.Success(Unit)
            } else {
                Result.Error(response.message ?: "비밀번호가 일치하지 않습니다.")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
        }
    }
}
