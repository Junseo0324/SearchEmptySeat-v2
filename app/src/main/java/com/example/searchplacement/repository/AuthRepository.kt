package com.example.searchplacement.repository

import android.util.Log
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.member.FindPasswordRequest
import com.example.searchplacement.data.member.LoginRequest
import com.example.searchplacement.data.member.LoginResponse
import com.example.searchplacement.data.member.MyInfoUpdateRequest
import com.example.searchplacement.data.member.SignUpRequest
import com.example.searchplacement.data.api.APIService
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthRepository @Inject constructor(private val apiService: APIService) {

    suspend fun login(request: LoginRequest): Response<ApiResponse<LoginResponse>> {
        return apiService.login(request)

    }

    suspend fun register(
        request: SignUpRequest,
        imageFile: MultipartBody.Part?
    ): ApiResponse<Map<String, Any>> {
        val json = Gson().toJson(request)

        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("data", json)
            .apply {
                imageFile?.let {
                    addPart(it)
                }
            }
            .build()

//        Log.d("TAG", "multipartBody: $multipartBody")
//        Log.d("TAG", "json: $json")
//        Log.d("TAG", "imageFile: $imageFile")
        // 4. 서버에 요청 보내기
        return apiService.registerUser(requestBody, imageFile)
    }

    suspend fun forgotPassword(request: FindPasswordRequest): Response<ApiResponse<Map<String, String>>> {
        return apiService.forgotPassword(request)
    }

    suspend fun updatePassword(userId: Long, newPassword: String,token: String): Response<ApiResponse<String>> {
        val body = mapOf("newPassword" to newPassword)
        return apiService.updatePassword("Bearer $token",userId, body)
    }

    suspend fun updateUserInfo(userId: Long, request: MyInfoUpdateRequest, imageFile: MultipartBody.Part?, token: String): Response<ApiResponse<Map<String, Any>>> {
        val json = Gson().toJson(request)
        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

        Log.d("TAG", "json: $json")
        Log.d("TAG", "imageFile: $imageFile")

        return apiService.updateUserInfo(
            userId = userId,
            data = requestBody,
            image = imageFile,
            token = "Bearer $token"
        )
    }


}

