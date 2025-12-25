package com.example.searchplacement.data.repository

import com.example.searchplacement.data.api.ReviewApiService
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.review.ReviewResponse
import com.example.searchplacement.domain.repository.ReviewRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val apiService: ReviewApiService
) : ReviewRepository {

    override suspend fun registerReview(
        requestJson: RequestBody,
        imageFiles: List<MultipartBody.Part>?
    ): ApiResponse<Map<String, Any>> {
        return try {
             val response = apiService.addReview(requestJson, imageFiles)
             if (response.isSuccessful && response.body() != null) {
                 response.body()!!
             } else {
                 ApiResponse("fail", "Failed to register review", null)
             }
        } catch (e: Exception) {
             ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun getReviewsByStore(
        storePK: Long
    ): ApiResponse<List<ReviewResponse>> {
        return try {
             val response = apiService.getReviewsByStore(storePK)
             if (response.isSuccessful && response.body() != null) {
                 response.body()!!
             } else {
                 ApiResponse("fail", "Failed to get reviews", null)
             }
        } catch (e: Exception) {
             ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }
}