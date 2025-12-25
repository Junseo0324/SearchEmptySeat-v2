package com.example.searchplacement.data.repository

import com.example.searchplacement.data.api.MenuSectionApiService
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.section.MenuSectionBulkUpdateRequest
import com.example.searchplacement.data.section.MenuSectionRequest
import com.example.searchplacement.data.section.MenuSectionResponse
import com.example.searchplacement.domain.repository.MenuSectionRepository
import javax.inject.Inject

class MenuSectionRepositoryImpl @Inject constructor(
    private val apiService: MenuSectionApiService
) : MenuSectionRepository {
    override suspend fun getSections(storePK: Long): ApiResponse<List<MenuSectionResponse>> {
        return try {
            val response = apiService.getMenuSectionsByStore(storePK)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                ApiResponse("fail", "Failed to get sections", null)
            }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun addSection(storePK: Long, request: MenuSectionRequest): ApiResponse<Map<String, Any>> {
        return try {
            val response = apiService.addSection(storePK, request)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                ApiResponse("fail", "Failed to add section", null)
            }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun updateSection(sectionPK: Long, request: MenuSectionRequest): ApiResponse<Map<String, Any>> {
        return try {
            val response = apiService.updateMenuSection(sectionPK, request)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                ApiResponse("fail", "Failed to update section", null)
            }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun deleteSection(sectionPK: Long): ApiResponse<Map<String, Any>> {
        return try {
            val response = apiService.deleteMenuSection(sectionPK)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                ApiResponse("fail", "Failed to delete section", null)
            }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun bulkUpdateSections(storePK: Long, requests: List<MenuSectionBulkUpdateRequest>): ApiResponse<List<MenuSectionResponse>> {
        return try {
            val response = apiService.bulkUpdateMenuSections(storePK, requests)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                ApiResponse("fail", "Failed to bulk update sections", null)
            }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }
}