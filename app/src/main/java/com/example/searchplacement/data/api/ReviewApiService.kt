package com.example.searchplacement.data.api

import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.review.ReviewResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ReviewApiService {
    @Multipart
    @POST("/api/review/add")
    suspend fun addReview(
        @Part("data") reviewData: RequestBody,
        @Part images: List<MultipartBody.Part>? = null
    ): Response<ApiResponse<Map<String, Any>>>

    @GET("/api/review/store/{storePK}")
    suspend fun getReviewsByStore(
        @Path("storePK") storePK: Long
    ): Response<ApiResponse<List<ReviewResponse>>>
}