package com.example.searchplacement.data.repository

import com.example.searchplacement.data.api.AuthApiService
import com.example.searchplacement.data.dto.login.LoginRequest
import com.example.searchplacement.data.dto.login.LoginResponse
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.member.FindPasswordRequest
import com.example.searchplacement.data.member.MyInfoUpdateRequest
import com.example.searchplacement.data.member.SignUpRequest
import com.example.searchplacement.domain.repository.AuthRepository
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(private val apiService: AuthApiService) : AuthRepository {

    override suspend fun login(request: LoginRequest): ApiResponse<LoginResponse> {
        return try {
            val response = apiService.login(request)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                ApiResponse("fail", "Login failed: ${response.code()}", null)
            }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun register(
        request: SignUpRequest,
        imageFile: MultipartBody.Part?
    ): ApiResponse<Map<String, Any>> {
        return try {
            val json = Gson().toJson(request)
            val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
            apiService.registerUser(requestBody, imageFile)
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun forgotPassword(request: FindPasswordRequest): ApiResponse<Map<String, String>> {
        return try {
            val response = apiService.forgotPassword(request)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                ApiResponse("fail", "Request failed: ${response.code()}", null)
            }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun updatePassword(userId: Long, newPassword: String): ApiResponse<String> {
        return try {
            val body = mapOf("newPassword" to newPassword)
            val response = apiService.updatePassword(userId, body)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                ApiResponse("fail", "Update failed: ${response.code()}", null)
            }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun updateUserInfo(userId: Long, request: MyInfoUpdateRequest, imageFile: MultipartBody.Part?): ApiResponse<Map<String, Any>> {
         return try {
            val json = Gson().toJson(request)
            val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
            val response = apiService.updateUserInfo(
                userId = userId,
                data = requestBody,
                image = imageFile
            )
             if (response.isSuccessful && response.body() != null) {
                 response.body()!!
             } else {
                 ApiResponse("fail", "Update failed: ${response.code()}", null)
             }
        } catch (e: Exception) {
             ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }
}
