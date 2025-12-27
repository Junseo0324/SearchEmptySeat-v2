package com.example.searchplacement.domain.usecase

import com.example.searchplacement.core.util.Result
import com.example.searchplacement.data.member.SignUpRequest
import com.example.searchplacement.domain.repository.AuthRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun execute(
        signUpRequest: SignUpRequest,
        imageFile: MultipartBody.Part?
    ): Result<Unit, String> {
        return try {
            val response = authRepository.register(signUpRequest, imageFile)
            if (response.status == "success") {
                Result.Success(Unit)
            } else {
                Result.Error(response.message ?: "회원가입 실패")
            }
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "회원가입 중 오류가 발생했습니다")
        }
    }
}
