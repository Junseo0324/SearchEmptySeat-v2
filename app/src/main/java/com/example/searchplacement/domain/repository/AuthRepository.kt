package com.example.searchplacement.domain.repository

import com.example.searchplacement.data.dto.login.LoginRequest
import com.example.searchplacement.data.dto.login.LoginResponse
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.member.FindPasswordRequest
import com.example.searchplacement.data.member.MyInfoUpdateRequest
import com.example.searchplacement.data.member.SignUpRequest
import okhttp3.MultipartBody

interface AuthRepository {
    suspend fun login(request: LoginRequest): ApiResponse<LoginResponse>
    suspend fun register(request: SignUpRequest, imageFile: MultipartBody.Part?): ApiResponse<Map<String, Any>>
    suspend fun forgotPassword(request: FindPasswordRequest): ApiResponse<Map<String, String>>
    suspend fun updatePassword(userId: Long, newPassword: String): ApiResponse<String>
    suspend fun updateUserInfo(userId: Long, request: MyInfoUpdateRequest, imageFile: MultipartBody.Part?): ApiResponse<Map<String, Any>>
}
