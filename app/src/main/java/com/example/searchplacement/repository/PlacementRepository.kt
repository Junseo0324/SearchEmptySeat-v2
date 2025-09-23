package com.example.searchplacement.repository

import com.example.searchplacement.data.api.PlacementApiService
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.placement.PlacementRequest
import com.example.searchplacement.data.placement.PlacementResponse
import com.example.searchplacement.data.placement.PlacementUpdateRequest
import retrofit2.Response
import javax.inject.Inject

class PlacementRepository @Inject constructor(private val apiService: PlacementApiService) {
    suspend fun createPlacement(request: PlacementRequest): Response<ApiResponse<PlacementResponse>> {
        return apiService.createPlacement(request)
    }

    suspend fun getPlacementByStore(storePK: Long): Response<ApiResponse<PlacementResponse>> {
        return apiService.getPlacementByStore(storePK)
    }

    suspend fun updatePlacement(placementPK: Long, request: PlacementUpdateRequest): Response<ApiResponse<PlacementResponse>> {
        return apiService.updatePlacement(placementPK, request)
    }
}