package com.example.searchplacement.domain.usecase

import android.content.Context
import android.net.Uri
import com.example.searchplacement.core.util.Result
import com.example.searchplacement.domain.model.ReviewRequest
import com.example.searchplacement.domain.repository.ReviewRepository
import com.example.searchplacement.presentation.utils.toMultipartPart
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class RegisterReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
    @ApplicationContext private val context: Context
) {
    suspend fun execute(
        storePK: Long,
        rating: Float,
        content: String,
        imageUris: List<Uri>
    ): Result<Unit, String> {
        return try {
            val request = ReviewRequest(
                storePK = storePK,
                rating = rating,
                content = content
            )
            
            val jsonString = Json.encodeToString(request)
            val requestBody = jsonString.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val imageParts = imageUris.map { uri ->
                 toMultipartPart(context, uri)
            }

            val response = reviewRepository.registerReview(requestBody, imageParts)
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
