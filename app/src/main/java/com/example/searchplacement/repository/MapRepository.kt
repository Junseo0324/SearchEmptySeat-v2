package com.example.searchplacement.repository

import com.example.searchplacement.data.api.APIService
import com.example.searchplacement.data.map.MapPinDetailResponse
import com.example.searchplacement.data.map.MapPinResponse
import com.example.searchplacement.data.member.ApiResponse
import retrofit2.Response
import javax.inject.Inject

class MapRepository @Inject constructor(
    private val apiService: APIService
) {
    suspend fun getMapPins(token: String): Response<ApiResponse<List<MapPinResponse>>> {
        return apiService.getMapPins("Bearer $token")
    }

    suspend fun getMapPinDetail(token: String, storePK: Long): Response<ApiResponse<MapPinDetailResponse>> {
        return apiService.getMapPinDetail("Bearer $token", storePK)
    }
}