package com.example.searchplacement.domain.usecase

import com.example.searchplacement.core.util.Result
import com.example.searchplacement.data.member.FindPasswordRequest
import com.example.searchplacement.domain.repository.AuthRepository
import javax.inject.Inject

class FindPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun execute(email: String): Result<Unit, String> {
        return try {
            val response = authRepository.forgotPassword(FindPasswordRequest(email))
            if (response.status == "success") {
                Result.Success(Unit)
            } else {
                Result.Error(response.message ?: "비밀번호 찾기에 실패했습니다.")
            }
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "네트워크 오류")
        }
    }
}
