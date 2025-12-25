package com.example.searchplacement.domain.repository

import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.review.ReviewResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ReviewRepository {
    suspend fun registerReview(requestJson: RequestBody, imageFiles: List<MultipartBody.Part>? = null): ApiResponse<Map<String, Any>>
    suspend fun getReviewsByStore(storePK: Long): ApiResponse<List<ReviewResponse>>
}
