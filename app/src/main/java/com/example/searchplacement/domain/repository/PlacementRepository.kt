package com.example.searchplacement.domain.repository

import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.placement.PlacementRequest
import com.example.searchplacement.data.placement.PlacementResponse
import com.example.searchplacement.data.placement.PlacementUpdateRequest

interface PlacementRepository {
    suspend fun createPlacement(request: PlacementRequest): ApiResponse<PlacementResponse>
    suspend fun getPlacementByStore(storePK: Long): ApiResponse<PlacementResponse>
    suspend fun updatePlacement(placementPK: Long, request: PlacementUpdateRequest): ApiResponse<PlacementResponse>
}
