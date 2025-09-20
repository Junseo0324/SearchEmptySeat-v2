package com.example.searchplacement.repository

import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.section.MenuSectionBulkUpdateRequest
import com.example.searchplacement.data.section.MenuSectionRequest
import com.example.searchplacement.data.section.MenuSectionResponse
import com.example.searchplacement.data.api.APIService
import retrofit2.Response
import javax.inject.Inject

class MenuSectionRepository @Inject constructor(
    private val apiService: APIService
) {
    suspend fun getSections(token: String, storePK: Long): Response<ApiResponse<List<MenuSectionResponse>>> {
        return apiService.getMenuSectionsByStore("Bearer $token", storePK)
    }

    suspend fun addSection(token: String, storePK: Long, request: MenuSectionRequest): Response<ApiResponse<Map<String, Any>>> {
        return apiService.addSection("Bearer $token", storePK, request)
    }
    suspend fun updateSection(token: String, sectionPK: Long, request: MenuSectionRequest): Response<ApiResponse<Map<String, Any>>> {
        return apiService.updateMenuSection("Bearer $token", sectionPK, request)
    }

    suspend fun deleteSection(token: String, sectionPK: Long): Response<ApiResponse<Map<String, Any>>> {
        return apiService.deleteMenuSection("Bearer $token", sectionPK)
    }

    suspend fun bulkUpdateSections(token: String, storePK: Long, requests: List<MenuSectionBulkUpdateRequest>): Response<ApiResponse<List<MenuSectionResponse>>> {
        return apiService.bulkUpdateMenuSections("Bearer $token", storePK, requests)
    }


}