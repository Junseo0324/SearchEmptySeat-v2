package com.example.searchplacement.domain.usecase

import com.example.searchplacement.core.util.Result
import com.example.searchplacement.data.placement.PlacementResponse
import com.example.searchplacement.domain.repository.PlacementRepository
import javax.inject.Inject

class GetPlacementUseCase @Inject constructor(
    private val placementRepository: PlacementRepository
) {
    suspend fun execute(storeId: Long): Result<PlacementResponse, String> {
        return try {
            val response = placementRepository.getPlacementByStore(storeId)
            if (response.status == "success" && response.data != null) {
                Result.Success(response.data)
            } else {
                Result.Error(response.message ?: "배치 정보를 불러오는데 실패했습니다.")
            }
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "네트워크 오류 발생")
        }
    }
}
