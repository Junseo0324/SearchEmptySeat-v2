package com.example.searchplacement.repository

import com.example.searchplacement.data.api.APIService
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.review.ReviewResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject


class ReviewRepository @Inject constructor(
    private val apiService: APIService
) {

    /** 리뷰 등록 */
    suspend fun registerReview(
        token: String,
        requestJson: RequestBody,
        imageFiles: List<MultipartBody.Part>? = null
    ): Response<ApiResponse<Map<String, Any>>> {
        return apiService.addReview("Bearer $token", requestJson, imageFiles)
    }

    /** 특정 가게의 리뷰 목록 조회 */
    suspend fun getReviewsByStore(
        token: String,
        storePK: Long
    ): Response<ApiResponse<List<ReviewResponse>>> {
        return apiService.getReviewsByStore("Bearer $token", storePK)
    }
}