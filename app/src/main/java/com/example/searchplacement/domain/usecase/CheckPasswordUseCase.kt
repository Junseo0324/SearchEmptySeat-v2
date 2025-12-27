package com.example.searchplacement.domain.usecase

import com.example.searchplacement.core.util.Result
import com.example.searchplacement.data.dto.login.LoginRequest
import com.example.searchplacement.domain.repository.AuthRepository
import com.example.searchplacement.domain.repository.UserRepository
import javax.inject.Inject

class CheckPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend fun execute(password: String): Result<Unit, String> {
        return try {
            val user = userRepository.getUser()
            val email = user?.email ?: return Result.Error("사용자 정보를 찾을 수 없습니다.")
            
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
