package com.example.searchplacement.repository

import com.example.searchplacement.data.api.StoreApiService
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.store.StoreResponse
import retrofit2.Response
import javax.inject.Inject

class StoreRepository @Inject constructor(
    private val apiService: StoreApiService
) {
    suspend fun getAllStores(sortBy: String): Response<ApiResponse<List<StoreResponse>>> {
        return apiService.getAllStores(sortBy)
    }

    suspend fun getStoreData(storeId: Long): Response<ApiResponse<StoreResponse>> {
        return apiService.getStoreData(storeId)
    }

    suspend fun getStoresByCategory(category: String, sortBy: String): Response<ApiResponse<List<StoreResponse>>> {
        return apiService.getStoresByCategory(category, sortBy)
    }

    suspend fun searchStoresByName(storeName: String): Response<ApiResponse<List<StoreResponse>>> {
        return apiService.searchStoresByName(storeName)
    }
}
