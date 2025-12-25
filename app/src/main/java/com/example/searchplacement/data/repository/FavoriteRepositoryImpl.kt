package com.example.searchplacement.data.repository

import com.example.searchplacement.data.api.FavoriteApiService
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.store.FavoriteResponse
import com.example.searchplacement.domain.repository.FavoriteRepository
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val apiService: FavoriteApiService
) : FavoriteRepository {
    override suspend fun addFavorite(storeId: Long): ApiResponse<FavoriteResponse> {
        return try {
             val response = apiService.addFavorite(storeId)
             if (response.isSuccessful && response.body() != null) {
                 response.body()!!
             } else {
                 ApiResponse("fail", "Failed to add favorite", null)
             }
        } catch (e: Exception) {
             ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun removeFavorite(storeId: Long): ApiResponse<String> {
        return try {
             val response = apiService.removeFavorite(storeId)
             if (response.isSuccessful && response.body() != null) {
                 response.body()!!
             } else {
                 ApiResponse("fail", "Failed to remove favorite", null)
             }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun getFavoriteList(userId: String): ApiResponse<List<FavoriteResponse>> {
        return try {
             val response = apiService.getFavoriteList(userId)
             if (response.isSuccessful && response.body() != null) {
                 response.body()!!
             } else {
                 ApiResponse("fail", "Failed to get favorites", null)
             }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }
}
