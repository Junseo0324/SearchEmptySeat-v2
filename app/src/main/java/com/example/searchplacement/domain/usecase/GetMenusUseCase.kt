package com.example.searchplacement.domain.usecase

import com.example.searchplacement.core.util.Result
import com.example.searchplacement.data.menu.MenuResponse
import com.example.searchplacement.domain.repository.MenuRepository
import javax.inject.Inject

class GetMenusUseCase @Inject constructor(
    private val menuRepository: MenuRepository
) {
    suspend fun execute(storeId: Long): Result<List<MenuResponse>, String> {
        return try {
            val response = menuRepository.getMenus(storeId)
            if (response.status == "success") {
                Result.Success(response.data ?: emptyList())
            } else {
                Result.Error(response.message ?: "메뉴를 불러오는데 실패했습니다.")
            }
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "네트워크 오류 발생")
        }
    }
}
