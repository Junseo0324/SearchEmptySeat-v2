package com.example.searchplacement.repository

import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.store.StoreRequest
import com.example.searchplacement.data.api.APIService
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class OwnerStoreRepository @Inject constructor(private val apiService: APIService) {
    suspend fun registerStore(token: String, storeRequest: StoreRequest, imageFiles: List<File>?): Response<ApiResponse<Map<String, Any>>> {
        val gson = Gson()
        val json = gson.toJson(storeRequest)
        val dataBody = json.toRequestBody("application/json; charset=UTF-8".toMediaType())
        val imageParts = imageFiles?.map { file ->
            val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("images", file.name, reqFile)
        }
        return apiService.registerStore("Bearer $token",dataBody, imageParts)
    }

    suspend fun getMyStores(token: String) = apiService.getMyStores("Bearer $token")

    suspend fun updateStore(token: String, storeId: Long, storeRequest: StoreRequest, imageFiles: List<File>?): Response<ApiResponse<Map<String, Any>>> {
        val gson = Gson()
        val json = gson.toJson(storeRequest)
        val dataBody = json.toRequestBody("application/json; charset=UTF-8".toMediaType())
        val imageParts = imageFiles?.map { file ->
            val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("images", file.name, reqFile)
        }
        return apiService.updateStore("Bearer $token", storeId, dataBody, imageParts)
    }
}
