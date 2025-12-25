package com.example.searchplacement.data.repository

import com.example.searchplacement.data.api.PlacementApiService
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.placement.PlacementRequest
import com.example.searchplacement.data.placement.PlacementResponse
import com.example.searchplacement.data.placement.PlacementUpdateRequest
import com.example.searchplacement.domain.repository.PlacementRepository
import javax.inject.Inject

class PlacementRepositoryImpl @Inject constructor(private val apiService: PlacementApiService) : PlacementRepository {
    override suspend fun createPlacement(request: PlacementRequest): ApiResponse<PlacementResponse> {
        return try {
             val response = apiService.createPlacement(request)
             if (response.isSuccessful && response.body() != null) {
                 response.body()!!
             } else {
                 ApiResponse("fail", "Failed to create placement", null)
             }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun getPlacementByStore(storePK: Long): ApiResponse<PlacementResponse> {
        return try {
             val response = apiService.getPlacementByStore(storePK)
             if (response.isSuccessful && response.body() != null) {
                 response.body()!!
             } else {
                 ApiResponse("fail", "Failed to get placement", null)
             }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun updatePlacement(placementPK: Long, request: PlacementUpdateRequest): ApiResponse<PlacementResponse> {
        return try {
             val response = apiService.updatePlacement(placementPK, request)
             if (response.isSuccessful && response.body() != null) {
                 response.body()!!
             } else {
                 ApiResponse("fail", "Failed to update placement", null)
             }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }
}