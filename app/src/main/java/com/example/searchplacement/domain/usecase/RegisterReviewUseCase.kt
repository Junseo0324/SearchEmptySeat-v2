package com.example.searchplacement.domain.usecase

import com.example.searchplacement.core.util.Result
import com.example.searchplacement.domain.model.ReviewRequest
import com.example.searchplacement.domain.repository.ReviewRepository
import javax.inject.Inject

class RegisterReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend fun execute(
        storePK: Long,
        rating: Float,
        content: String,
        imageUris: List<String>
    ): Result<Unit, String> {
        return try {
            val request = ReviewRequest(
                storePK = storePK,
                rating = rating,
                content = content
            )
            
            val response = reviewRepository.registerReview(request, imageUris)
            if (response.status == "success") {
                Result.Success(Unit)
            } else {
                Result.Error(response.message ?: "리뷰 등록 실패")
            }
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "네트워크 오류 발생")
        }
    }
}
