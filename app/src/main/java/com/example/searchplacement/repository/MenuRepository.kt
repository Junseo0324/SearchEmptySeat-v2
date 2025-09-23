package com.example.searchplacement.repository

import com.example.searchplacement.data.api.MenuApiService
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.menu.MenuRequest
import com.example.searchplacement.data.menu.MenuResponse
import com.example.searchplacement.data.menu.OutOfStockRequest
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class MenuRepository @Inject constructor(
    private val apiService: MenuApiService
) {
    /** 가게의 전체 메뉴 조회 */
    suspend fun getMenus(storePK: Long): Response<ApiResponse<List<MenuResponse>>> {
        return apiService.getMenus(storePK)
    }

    /** 메뉴 추가 */
    suspend fun addMenu(menuRequest: MenuRequest, imageFile: File?): Response<ApiResponse<Map<String, Any>>> {
        val gson = Gson()
        val json = gson.toJson(menuRequest)
        val dataBody = json.toRequestBody("application/json; charset=UTF-8".toMediaType())
        val imagePart = imageFile?.let {
            val reqFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", it.name, reqFile)
        }
        return apiService.addMenu(dataBody, imagePart)
    }

    /** 메뉴 수정 */
    suspend fun updateMenu(menuId: Long, menuRequest: MenuRequest, imageFile: File?): Response<ApiResponse<Map<String, Any>>> {
        val gson = Gson()
        val json = gson.toJson(menuRequest)
        val dataBody = json.toRequestBody("application/json; charset=UTF-8".toMediaType())
        val imagePart = imageFile?.let {
            val reqFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", it.name, reqFile)
        }
        return apiService.updateMenu(menuId, dataBody, imagePart)
    }

    /** 메뉴 삭제 */
    suspend fun deleteMenu(menuId: Long): Response<ApiResponse<Map<String, Any>>> {
        return apiService.deleteMenu(menuId)
    }

    /** 품절 처리 (복수) */
    suspend fun updateMenusStock(request: OutOfStockRequest): Response<ApiResponse<List<Map<String, Any>>>> {
        return apiService.updateMenusStock(request)
    }
}