package com.example.searchplacement.data.api

import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.placement.PlacementRequest
import com.example.searchplacement.data.placement.PlacementResponse
import com.example.searchplacement.data.placement.PlacementUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PlacementApiService {
    //자리 배치
    /** 자리 배치 생성 */
    @POST("/api/placement")
    suspend fun createPlacement(
        @Body request: PlacementRequest
    ): Response<ApiResponse<PlacementResponse>>

    /** 매장별 자리배치 조회 */
    @GET("/api/placement/store/{storePK}")
    suspend fun getPlacementByStore(
        @Path("storePK") storePK: Long
    ): Response<ApiResponse<PlacementResponse>>

    /** 자리 배치 업데이트 (테이블 상태 변경) */
    @PUT("/api/placement/{placementPK}")
    suspend fun updatePlacement(
        @Path("placementPK") placementPK: Long,
        @Body request: PlacementUpdateRequest
    ): Response<ApiResponse<PlacementResponse>>
}