package com.example.searchplacement.domain.repository

import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.menu.MenuRequest
import com.example.searchplacement.data.menu.MenuResponse
import com.example.searchplacement.data.menu.OutOfStockRequest
import java.io.File

interface MenuRepository {
    suspend fun getMenus(storePK: Long): ApiResponse<List<MenuResponse>>
    suspend fun addMenu(menuRequest: MenuRequest, imageFile: File?): ApiResponse<Map<String, Any>>
    suspend fun updateMenu(menuId: Long, menuRequest: MenuRequest, imageFile: File?): ApiResponse<Map<String, Any>>
    suspend fun deleteMenu(menuId: Long): ApiResponse<Map<String, Any>>
    suspend fun updateMenusStock(request: OutOfStockRequest): ApiResponse<List<Map<String, Any>>>
}
