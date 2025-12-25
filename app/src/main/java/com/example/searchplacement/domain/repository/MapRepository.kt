package com.example.searchplacement.domain.repository

import com.example.searchplacement.data.map.MapPinDetailResponse
import com.example.searchplacement.data.map.MapPinResponse
import com.example.searchplacement.data.member.ApiResponse

interface MapRepository {
    suspend fun getMapPins(): ApiResponse<List<MapPinResponse>>
    suspend fun getMapPinDetail(storePK: Long): ApiResponse<MapPinDetailResponse>
}
