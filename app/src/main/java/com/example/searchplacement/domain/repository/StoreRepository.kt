package com.example.searchplacement.domain.repository

import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.store.StoreResponse

interface StoreRepository {
    suspend fun getAllStores(sortBy: String): ApiResponse<List<StoreResponse>>
    suspend fun getStoreData(storeId: Long): ApiResponse<StoreResponse>
    suspend fun getStoresByCategory(category: String, sortBy: String): ApiResponse<List<StoreResponse>>
    suspend fun searchStoresByName(storeName: String): ApiResponse<List<StoreResponse>>
}
