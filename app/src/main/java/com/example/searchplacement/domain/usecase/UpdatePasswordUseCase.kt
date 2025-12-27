package com.example.searchplacement.domain.usecase

import com.example.searchplacement.core.util.Result
import com.example.searchplacement.domain.repository.AuthRepository
import com.example.searchplacement.domain.repository.UserRepository
import javax.inject.Inject

class UpdatePasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend fun execute(newPassword: String): Result<Unit, String> {
        return try {
            val user = userRepository.getUser()
            val userId = user?.userId?.toLong() ?: return Result.Error("사용자 정보를 찾을 수 없습니다.")

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
