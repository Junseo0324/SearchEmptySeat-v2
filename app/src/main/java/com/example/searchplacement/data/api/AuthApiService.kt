package com.example.searchplacement.data.api

import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.member.FindPasswordRequest
import com.example.searchplacement.data.member.LoginRequest
import com.example.searchplacement.data.member.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface AuthApiService {
    @Multipart
    @POST("/api/auth/signup")
    suspend fun registerUser(
        @Part("data") userData: RequestBody,
        @Part image: MultipartBody.Part?
    ): ApiResponse<Map<String, Any>>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<LoginResponse>>

    @POST("/api/auth/forgot-password")
    suspend fun forgotPassword(@Body request: FindPasswordRequest): Response<ApiResponse<Map<String, String>>>

    @PATCH("api/member/{userId}/password")
    suspend fun updatePassword(
        @Path("userId") userId: Long,
        @Body passwordMap: Map<String, String>
    ): Response<ApiResponse<String>>

    @Multipart
    @PATCH("api/member/{userId}")
    suspend fun updateUserInfo(
        @Path("userId") userId: Long,
        @Part("data") data: RequestBody,
        @Part image: MultipartBody.Part? = null,
    ): Response<ApiResponse<Map<String, Any>>>
}