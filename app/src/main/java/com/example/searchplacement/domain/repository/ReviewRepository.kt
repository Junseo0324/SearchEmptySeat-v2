package com.example.searchplacement.domain.repository

import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.review.ReviewResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

import com.example.searchplacement.domain.model.ReviewRequest

interface ReviewRepository {
    suspend fun registerReview(request: ReviewRequest, imageUris: List<String>): ApiResponse<Map<String, Any>>
    suspend fun getReviewsByStore(storePK: Long): ApiResponse<List<ReviewResponse>>
}
