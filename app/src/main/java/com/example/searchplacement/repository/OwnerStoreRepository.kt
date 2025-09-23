package com.example.searchplacement.repository

import com.example.searchplacement.data.api.StoreApiService
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.store.StoreRequest
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class OwnerStoreRepository @Inject constructor(private val apiService: StoreApiService) {
    suspend fun registerStore(storeRequest: StoreRequest, imageFiles: List<File>?): Response<ApiResponse<Map<String, Any>>> {
        val gson = Gson()
        val json = gson.toJson(storeRequest)
        val dataBody = json.toRequestBody("application/json; charset=UTF-8".toMediaType())
        val imageParts = imageFiles?.map { file ->
            val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("images", file.name, reqFile)
        }
        return apiService.registerStore(dataBody, imageParts)
    }

    suspend fun getMyStores() = apiService.getMyStores()

    suspend fun updateStore(storeId: Long, storeRequest: StoreRequest, imageFiles: List<File>?): Response<ApiResponse<Map<String, Any>>> {
        val gson = Gson()
        val json = gson.toJson(storeRequest)
        val dataBody = json.toRequestBody("application/json; charset=UTF-8".toMediaType())
        val imageParts = imageFiles?.map { file ->
            val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("images", file.name, reqFile)
        }
        return apiService.updateStore(storeId, dataBody, imageParts)
    }
}
