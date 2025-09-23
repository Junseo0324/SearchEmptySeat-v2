package com.example.searchplacement.data.api

import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.menu.MenuResponse
import com.example.searchplacement.data.menu.OutOfStockRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface MenuApiService {
    //메뉴 관련
    /** 메뉴 추가 */
    @Multipart
    @POST("/api/menu/add")
    suspend fun addMenu(
        @Part("data") menuData: RequestBody,
        @Part image: MultipartBody.Part? = null
    ): Response<ApiResponse<Map<String, Any>>>


    /** 메뉴 수정 */
    @Multipart
    @PUT("/api/menu/update/{menuId}")
    suspend fun updateMenu(
        @Path("menuId") menuId: Long,
        @Part("data") data: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<ApiResponse<Map<String, Any>>>

    /** 메뉴 삭제 */
    @DELETE("/api/menu/del/{menuId}")
    suspend fun deleteMenu(
        @Path("menuId") menuId: Long
    ): Response<ApiResponse<Map<String, Any>>>

    /** 전체 메뉴 조회 */
    @GET("/api/menu/store/{storePK}")
    suspend fun getMenus(
        @Path("storePK") storePK: Long
    ): Response<ApiResponse<List<MenuResponse>>>

    /** 품절 */
    @PATCH("/api/menu/outofstock")
    suspend fun updateMenusStock(
        @Body request: OutOfStockRequest
    ): Response<ApiResponse<List<Map<String, Any>>>>
}