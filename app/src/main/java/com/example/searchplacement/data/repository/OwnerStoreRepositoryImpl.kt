package com.example.searchplacement.data.repository

import com.example.searchplacement.data.api.StoreApiService
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.store.StoreRequest
import com.example.searchplacement.data.store.StoreResponse
import com.example.searchplacement.domain.repository.OwnerStoreRepository
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class OwnerStoreRepositoryImpl @Inject constructor(private val apiService: StoreApiService) : OwnerStoreRepository {
    override suspend fun registerStore(storeRequest: StoreRequest, imageFiles: List<File>?): ApiResponse<Map<String, Any>> {
        return try {
            val gson = Gson()
            val json = gson.toJson(storeRequest)
            val dataBody = json.toRequestBody("application/json; charset=UTF-8".toMediaType())
            val imageParts = imageFiles?.map { file ->
                val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("images", file.name, reqFile)
            }
            val response = apiService.registerStore(dataBody, imageParts)
             if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                ApiResponse("fail", "Failed to register store", null)
            }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun getMyStores(): ApiResponse<List<StoreResponse>> {
        return try {
            val response = apiService.getMyStores()
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                 ApiResponse("fail", "Failed to get my stores", null)
            }
        } catch (e: Exception) {
             ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun updateStore(storeId: Long, storeRequest: StoreRequest, imageFiles: List<File>?): ApiResponse<Map<String, Any>> {
        return try {
            val gson = Gson()
            val json = gson.toJson(storeRequest)
            val dataBody = json.toRequestBody("application/json; charset=UTF-8".toMediaType())
            val imageParts = imageFiles?.map { file ->
                val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("images", file.name, reqFile)
            }
            val response = apiService.updateStore(storeId, dataBody, imageParts)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                ApiResponse("fail", "Failed to update store", null)
            }
        } catch (e: Exception) {
             ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }
}
