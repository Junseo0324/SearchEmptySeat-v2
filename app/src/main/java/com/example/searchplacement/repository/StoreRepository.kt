package com.example.searchplacement.repository

import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.store.StoreResponse
import com.example.searchplacement.data.api.APIService
import retrofit2.Response
import javax.inject.Inject

class StoreRepository @Inject constructor(
    private val apiService: APIService
) {
    suspend fun getAllStores(token: String, sortBy: String): Response<ApiResponse<List<StoreResponse>>> {
        return apiService.getAllStores("Bearer $token", sortBy)
    }

    suspend fun getStoreData(token: String, storeId: Long): Response<ApiResponse<StoreResponse>> {
        return apiService.getStoreData("Bearer $token",storeId)
    }

    suspend fun getStoresByCategory(token: String, category: String, sortBy: String): Response<ApiResponse<List<StoreResponse>>> {
        return apiService.getStoresByCategory("Bearer $token", category, sortBy)
    }

    suspend fun searchStoresByName(token: String,storeName: String): Response<ApiResponse<List<StoreResponse>>> {
        return apiService.searchStoresByName("Bearer $token",storeName)
    }
}
