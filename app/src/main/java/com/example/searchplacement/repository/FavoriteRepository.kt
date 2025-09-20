package com.example.searchplacement.repository

import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.store.FavoriteResponse
import com.example.searchplacement.data.api.APIService
import retrofit2.Response
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val apiService: APIService
) {
    suspend fun addFavorite(token: String, storeId: Long): Response<ApiResponse<FavoriteResponse>> {
        return apiService.addFavorite("Bearer $token", storeId)
    }

    suspend fun removeFavorite(token: String, storeId: Long): Response<ApiResponse<String>> {
        return apiService.removeFavorite("Bearer $token", storeId)
    }

    suspend fun getFavoriteList(token: String, userId: String): Response<ApiResponse<List<FavoriteResponse>>> {
        return apiService.getFavoriteList("Bearer $token", userId)
    }
}
