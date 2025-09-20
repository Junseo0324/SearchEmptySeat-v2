package com.example.searchplacement.repository

import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.placement.PlacementRequest
import com.example.searchplacement.data.placement.PlacementResponse
import com.example.searchplacement.data.placement.PlacementUpdateRequest
import com.example.searchplacement.data.api.APIService
import retrofit2.Response
import javax.inject.Inject

class PlacementRepository @Inject constructor(private val apiService: APIService) {
    suspend fun createPlacement(token: String, request: PlacementRequest): Response<ApiResponse<PlacementResponse>> {
        return apiService.createPlacement("Bearer $token", request)
    }

    suspend fun getPlacementByStore(token: String, storePK: Long): Response<ApiResponse<PlacementResponse>> {
        return apiService.getPlacementByStore("Bearer $token", storePK)
    }

    suspend fun updatePlacement(token: String, placementPK: Long, request: PlacementUpdateRequest): Response<ApiResponse<PlacementResponse>> {
        return apiService.updatePlacement("Bearer $token", placementPK, request)
    }
}