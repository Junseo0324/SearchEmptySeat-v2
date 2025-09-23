package com.example.searchplacement.repository

import com.example.searchplacement.data.api.AuthApiService
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.member.FindPasswordRequest
import com.example.searchplacement.data.member.LoginRequest
import com.example.searchplacement.data.member.LoginResponse
import com.example.searchplacement.data.member.MyInfoUpdateRequest
import com.example.searchplacement.data.member.SignUpRequest
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthRepository @Inject constructor(private val apiService: AuthApiService) {

    suspend fun login(request: LoginRequest): Response<ApiResponse<LoginResponse>> {
        return apiService.login(request)

    }

    suspend fun register(
        request: SignUpRequest,
        imageFile: MultipartBody.Part?
    ): ApiResponse<Map<String, Any>> {
        val json = Gson().toJson(request)

        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
        return apiService.registerUser(requestBody, imageFile)
    }

    suspend fun forgotPassword(request: FindPasswordRequest): Response<ApiResponse<Map<String, String>>> {
        return apiService.forgotPassword(request)
    }

    suspend fun updatePassword(userId: Long, newPassword: String): Response<ApiResponse<String>> {
        val body = mapOf("newPassword" to newPassword)
        return apiService.updatePassword(userId, body)
    }

    suspend fun updateUserInfo(userId: Long, request: MyInfoUpdateRequest, imageFile: MultipartBody.Part?): Response<ApiResponse<Map<String, Any>>> {
        val json = Gson().toJson(request)
        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())


        return apiService.updateUserInfo(
            userId = userId,
            data = requestBody,
            image = imageFile
        )
    }


}

