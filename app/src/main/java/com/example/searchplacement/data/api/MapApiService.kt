package com.example.searchplacement.data.api

import com.example.searchplacement.data.map.MapPinDetailResponse
import com.example.searchplacement.data.map.MapPinResponse
import com.example.searchplacement.data.member.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MapApiService {
    //Pin 관련
    @GET("/api/map/pins")
    suspend fun getMapPins(
    ): Response<ApiResponse<List<MapPinResponse>>>

    @GET("/api/map/pin/{storePK}")
    suspend fun getMapPinDetail(
        @Path("storePK") storePK: Long
    ): Response<ApiResponse<MapPinDetailResponse>>

}