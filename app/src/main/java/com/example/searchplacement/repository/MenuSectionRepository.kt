package com.example.searchplacement.repository

import com.example.searchplacement.data.api.MenuSectionApiService
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.section.MenuSectionBulkUpdateRequest
import com.example.searchplacement.data.section.MenuSectionRequest
import com.example.searchplacement.data.section.MenuSectionResponse
import retrofit2.Response
import javax.inject.Inject

class MenuSectionRepository @Inject constructor(
    private val apiService: MenuSectionApiService
) {
    suspend fun getSections(storePK: Long): Response<ApiResponse<List<MenuSectionResponse>>> {
        return apiService.getMenuSectionsByStore(storePK)
    }

    suspend fun addSection(storePK: Long, request: MenuSectionRequest): Response<ApiResponse<Map<String, Any>>> {
        return apiService.addSection(storePK, request)
    }
    suspend fun updateSection(sectionPK: Long, request: MenuSectionRequest): Response<ApiResponse<Map<String, Any>>> {
        return apiService.updateMenuSection(sectionPK, request)
    }

    suspend fun deleteSection(sectionPK: Long): Response<ApiResponse<Map<String, Any>>> {
        return apiService.deleteMenuSection(sectionPK)
    }

    suspend fun bulkUpdateSections(storePK: Long, requests: List<MenuSectionBulkUpdateRequest>): Response<ApiResponse<List<MenuSectionResponse>>> {
        return apiService.bulkUpdateMenuSections(storePK, requests)
    }


}