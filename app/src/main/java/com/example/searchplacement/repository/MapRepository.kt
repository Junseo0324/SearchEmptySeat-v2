package com.example.searchplacement.repository

import com.example.searchplacement.data.api.MapApiService
import com.example.searchplacement.data.map.MapPinDetailResponse
import com.example.searchplacement.data.map.MapPinResponse
import com.example.searchplacement.data.member.ApiResponse
import retrofit2.Response
import javax.inject.Inject

class MapRepository @Inject constructor(
    private val apiService: MapApiService
) {
    suspend fun getMapPins(): Response<ApiResponse<List<MapPinResponse>>> {
        return apiService.getMapPins()
    }

    suspend fun getMapPinDetail(storePK: Long): Response<ApiResponse<MapPinDetailResponse>> {
        return apiService.getMapPinDetail(storePK)
    }
}