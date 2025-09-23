package com.example.searchplacement.data.api

import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.store.FavoriteResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface FavoriteApiService {

    /** 즐겨찾기 */
    @POST("/api/favorites/{storeId}")
    suspend fun addFavorite(
        @Header("Authorization") token: String,
        @Path("storeId") storeId: Long
    ): Response<ApiResponse<FavoriteResponse>>

    @DELETE("/api/favorites/{storeId}")
    suspend fun removeFavorite(
        @Header("Authorization") token: String,
        @Path("storeId") storeId: Long
    ): Response<ApiResponse<String>>

    @GET("/api/favorites/{userId}")
    suspend fun getFavoriteList(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Response<ApiResponse<List<FavoriteResponse>>>
}