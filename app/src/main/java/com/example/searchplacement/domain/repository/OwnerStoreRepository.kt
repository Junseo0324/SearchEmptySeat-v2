package com.example.searchplacement.domain.repository

import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.store.StoreRequest
import com.example.searchplacement.data.store.StoreResponse
import java.io.File

interface OwnerStoreRepository {
    suspend fun registerStore(storeRequest: StoreRequest, imageFiles: List<File>?): ApiResponse<Map<String, Any>>
    suspend fun getMyStores(): ApiResponse<List<StoreResponse>>
    suspend fun updateStore(storeId: Long, storeRequest: StoreRequest, imageFiles: List<File>?): ApiResponse<Map<String, Any>>
}
