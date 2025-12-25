package com.example.searchplacement.domain.repository

import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.store.FavoriteResponse

interface FavoriteRepository {
    suspend fun addFavorite(storeId: Long): ApiResponse<FavoriteResponse>
    suspend fun removeFavorite(storeId: Long): ApiResponse<String>
    suspend fun getFavoriteList(userId: String): ApiResponse<List<FavoriteResponse>>
}
