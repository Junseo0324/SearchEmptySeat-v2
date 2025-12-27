package com.example.searchplacement.domain.usecase

import com.example.searchplacement.core.util.Result
import com.example.searchplacement.data.mapper.toModel
import com.example.searchplacement.domain.model.MapPinDetailModel
import com.example.searchplacement.domain.repository.MapRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetMapPinDetailUseCase @Inject constructor(
    private val mapRepository: MapRepository
) {
    suspend fun execute(storePK: Long): Result<MapPinDetailModel, String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = mapRepository.getMapPinDetail(storePK)
                if (response.status == "success" && response.data != null) {
                    Result.Success(response.data.toModel())
                } else {
                    Result.Error(response.message ?: "가게 정보를 불러오는데 실패했습니다.")
                }
            } catch (e: Exception) {
                Result.Error("네트워크 오류: ${e.message}")
            }
        }
    }
}
