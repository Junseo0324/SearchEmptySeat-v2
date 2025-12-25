package com.example.searchplacement.data.repository

import com.example.searchplacement.data.api.MenuApiService
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.menu.MenuRequest
import com.example.searchplacement.data.menu.MenuResponse
import com.example.searchplacement.data.menu.OutOfStockRequest
import com.example.searchplacement.domain.repository.MenuRepository
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class MenuRepositoryImpl @Inject constructor(
    private val apiService: MenuApiService
) : MenuRepository {
    override suspend fun getMenus(storePK: Long): ApiResponse<List<MenuResponse>> {
        return try {
             val response = apiService.getMenus(storePK)
             if (response.isSuccessful && response.body() != null) {
                 response.body()!!
             } else {
                 ApiResponse("fail", "Failed to get menus", null)
             }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun addMenu(menuRequest: MenuRequest, imageFile: File?): ApiResponse<Map<String, Any>> {
        return try {
             val gson = Gson()
             val json = gson.toJson(menuRequest)
             val dataBody = json.toRequestBody("application/json; charset=UTF-8".toMediaType())
             val imagePart = imageFile?.let {
                 val reqFile = it.asRequestBody("image/*".toMediaTypeOrNull())
                 MultipartBody.Part.createFormData("image", it.name, reqFile)
             }
             val response = apiService.addMenu(dataBody, imagePart)
             if (response.isSuccessful && response.body() != null) {
                 response.body()!!
             } else {
                 ApiResponse("fail", "Failed to add menu", null)
             }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun updateMenu(menuId: Long, menuRequest: MenuRequest, imageFile: File?): ApiResponse<Map<String, Any>> {
        return try {
             val gson = Gson()
             val json = gson.toJson(menuRequest)
             val dataBody = json.toRequestBody("application/json; charset=UTF-8".toMediaType())
             val imagePart = imageFile?.let {
                 val reqFile = it.asRequestBody("image/*".toMediaTypeOrNull())
                 MultipartBody.Part.createFormData("image", it.name, reqFile)
             }
             val response = apiService.updateMenu(menuId, dataBody, imagePart)
             if (response.isSuccessful && response.body() != null) {
                 response.body()!!
             } else {
                 ApiResponse("fail", "Failed to update menu", null)
             }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun deleteMenu(menuId: Long): ApiResponse<Map<String, Any>> {
        return try {
            val response = apiService.deleteMenu(menuId)
             if (response.isSuccessful && response.body() != null) {
                 response.body()!!
             } else {
                 ApiResponse("fail", "Failed to delete menu", null)
             }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun updateMenusStock(request: OutOfStockRequest): ApiResponse<List<Map<String, Any>>> {
        return try {
             val response = apiService.updateMenusStock(request)
             if (response.isSuccessful && response.body() != null) {
                 response.body()!!
             } else {
                 ApiResponse("fail", "Failed to update stock", null)
             }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }
}