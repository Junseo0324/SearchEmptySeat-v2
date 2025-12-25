package com.example.searchplacement.data.repository

import com.example.searchplacement.data.api.StoreApiService
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.store.StoreResponse
import com.example.searchplacement.domain.repository.StoreRepository
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val apiService: StoreApiService
) : StoreRepository {
    override suspend fun getAllStores(sortBy: String): ApiResponse<List<StoreResponse>> {
        return try {
            val response = apiService.getAllStores(sortBy)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                ApiResponse("fail", "Failed to get stores", null)
            }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun getStoreData(storeId: Long): ApiResponse<StoreResponse> {
        return try {
            val response = apiService.getStoreData(storeId)
             if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                ApiResponse("fail", "Failed to get store data", null)
            }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun getStoresByCategory(category: String, sortBy: String): ApiResponse<List<StoreResponse>> {
        return try {
            val response = apiService.getStoresByCategory(category, sortBy)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                ApiResponse("fail", "Failed to get stores by category", null)
            }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun searchStoresByName(storeName: String): ApiResponse<List<StoreResponse>> {
        return try {
            val response = apiService.searchStoresByName(storeName)
             if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                ApiResponse("fail", "Failed to search stores", null)
            }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }
}
