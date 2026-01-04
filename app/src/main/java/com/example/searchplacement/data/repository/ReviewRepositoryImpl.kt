package com.example.searchplacement.data.repository

import com.example.searchplacement.data.api.ReviewApiService
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.review.ReviewResponse
import com.example.searchplacement.domain.repository.ReviewRepository
import android.content.Context
import android.net.Uri
import com.example.searchplacement.domain.model.ReviewRequest
import com.example.searchplacement.presentation.utils.toMultipartPart
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody.Part
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val apiService: ReviewApiService,
    @ApplicationContext private val context: Context
) : ReviewRepository {

    override suspend fun registerReview(
        request: ReviewRequest,
        imageUris: List<String>
    ): ApiResponse<Map<String, Any>> {
        return try {
             val jsonString = Json.encodeToString(request)
             val requestBody = jsonString.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

             val imageParts = imageUris.map { uriString ->
                 val uri = Uri.parse(uriString)
                 toMultipartPart(context, uri)
             }

             val response = apiService.addReview(requestBody, imageParts)
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