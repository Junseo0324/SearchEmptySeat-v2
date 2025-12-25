package com.example.searchplacement.data.repository

import com.example.searchplacement.data.api.MapApiService
import com.example.searchplacement.data.map.MapPinDetailResponse
import com.example.searchplacement.data.map.MapPinResponse
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.domain.repository.MapRepository
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    private val apiService: MapApiService
) : MapRepository {
    override suspend fun getMapPins(): ApiResponse<List<MapPinResponse>> {
        return try {
             val response = apiService.getMapPins()
             if (response.isSuccessful && response.body() != null) {
                 response.body()!!
             } else {
                 ApiResponse("fail", "Failed to get map pins", null)
             }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun getMapPinDetail(storePK: Long): ApiResponse<MapPinDetailResponse> {
        return try {
             val response = apiService.getMapPinDetail(storePK)
             if (response.isSuccessful && response.body() != null) {
                 response.body()!!
             } else {
                 ApiResponse("fail", "Failed to get store detail", null)
             }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }
}