package com.example.searchplacement.data.api

import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.section.MenuSectionBulkUpdateRequest
import com.example.searchplacement.data.section.MenuSectionRequest
import com.example.searchplacement.data.section.MenuSectionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MenuSectionApiService {
    //메뉴 섹션
    // 메뉴 섹션 전체 조회
    @GET("/api/menu-section/store/{storePK}")
    suspend fun getMenuSectionsByStore(
        @Header("Authorization") token: String,
        @Path("storePK") storePK: Long
    ): Response<ApiResponse<List<MenuSectionResponse>>>

    //섹션 단일 추가
    @POST("/api/menu-section/add/{storePK}")
    suspend fun addSection(
        @Header("Authorization") token: String,
        @Path("storePK") storePK: Long,
        @Body request: MenuSectionRequest
    ): Response<ApiResponse<Map<String, Any>>>

    // 메뉴 섹션 개별 업데이트
    @PUT("/api/menu-section/update/{sectionPK}")
    suspend fun updateMenuSection(
        @Header("Authorization") token: String,
        @Path("sectionPK") sectionPK: Long,
        @Body request: MenuSectionRequest
    ): Response<ApiResponse<Map<String, Any>>>

    // 메뉴 섹션 삭제
    @DELETE("/api/menu-section/delete/{sectionPK}")
    suspend fun deleteMenuSection(
        @Header("Authorization") token: String,
        @Path("sectionPK") sectionPK: Long
    ): Response<ApiResponse<Map<String, Any>>>

    // 메뉴 섹션 일괄 업데이트
    @PUT("/api/menu-section/bulk-update/{storePK}")
    suspend fun bulkUpdateMenuSections(
        @Header("Authorization") token: String,
        @Path("storePK") storePK: Long,
        @Body request: List<MenuSectionBulkUpdateRequest>
    ): Response<ApiResponse<List<MenuSectionResponse>>>


}