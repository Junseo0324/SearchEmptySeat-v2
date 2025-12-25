package com.example.searchplacement.domain.repository

import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.section.MenuSectionBulkUpdateRequest
import com.example.searchplacement.data.section.MenuSectionRequest
import com.example.searchplacement.data.section.MenuSectionResponse

interface MenuSectionRepository {
    suspend fun getSections(storePK: Long): ApiResponse<List<MenuSectionResponse>>
    suspend fun addSection(storePK: Long, request: MenuSectionRequest): ApiResponse<Map<String, Any>>
    suspend fun updateSection(sectionPK: Long, request: MenuSectionRequest): ApiResponse<Map<String, Any>>
    suspend fun deleteSection(sectionPK: Long): ApiResponse<Map<String, Any>>
    suspend fun bulkUpdateSections(storePK: Long, requests: List<MenuSectionBulkUpdateRequest>): ApiResponse<List<MenuSectionResponse>>
}
