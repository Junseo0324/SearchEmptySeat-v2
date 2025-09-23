package com.example.searchplacement.repository

import com.example.searchplacement.data.api.FavoriteApiService
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.store.FavoriteResponse
import retrofit2.Response
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val apiService: FavoriteApiService
) {
    suspend fun addFavorite(storeId: Long): Response<ApiResponse<FavoriteResponse>> {
        return apiService.addFavorite(storeId)
    }

    suspend fun removeFavorite(storeId: Long): Response<ApiResponse<String>> {
        return apiService.removeFavorite(storeId)
    }

    suspend fun getFavoriteList(userId: String): Response<ApiResponse<List<FavoriteResponse>>> {
        return apiService.getFavoriteList(userId)
    }
}
