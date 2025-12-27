package com.example.searchplacement.domain.usecase

import com.example.searchplacement.core.util.Result
import com.example.searchplacement.domain.repository.AuthRepository
import javax.inject.Inject

class UpdatePasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun execute(userId: Long, newPassword: String): Result<Unit, String> {
        return try {
            val response = authRepository.updatePassword(userId, newPassword)
            if (response.status == "success") {
                Result.Success(Unit)
            } else {
                Result.Error(response.message ?: "비밀번호 변경에 실패했습니다.")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "알 수 없는 오류가 발생했습니다.")
        }
    }
}
