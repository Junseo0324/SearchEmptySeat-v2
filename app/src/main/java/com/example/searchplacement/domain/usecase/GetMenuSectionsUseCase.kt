package com.example.searchplacement.domain.usecase

import com.example.searchplacement.core.util.Result
import com.example.searchplacement.data.section.MenuSectionResponse
import com.example.searchplacement.domain.repository.MenuSectionRepository
import javax.inject.Inject

class GetMenuSectionsUseCase @Inject constructor(
    private val menuSectionRepository: MenuSectionRepository
) {
    suspend fun execute(storeId: Long): Result<List<MenuSectionResponse>, String> {
        return try {
            val response = menuSectionRepository.getSections(storeId)
            if (response.status == "success") {
                Result.Success(response.data ?: emptyList())
            } else {
                Result.Error(response.message ?: "메뉴 섹션을 불러오는데 실패했습니다.")
            }
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "네트워크 오류 발생")
        }
    }
}
