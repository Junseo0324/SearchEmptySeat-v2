package com.example.searchplacement.domain.usecase

import com.example.searchplacement.core.util.Result
import com.example.searchplacement.data.mapper.toModel
import com.example.searchplacement.domain.model.StoreModel
import com.example.searchplacement.domain.repository.StoreRepository
import javax.inject.Inject

class GetStoreDetailUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend fun execute(storeId: Long): Result<StoreModel, String> {
        return try {
            val response = storeRepository.getStoreData(storeId)
            if (response.status == "success" && response.data != null) {
                Result.Success(response.data.toModel())
            } else {
                Result.Error("매장 정보를 불러오는데 실패했습니다.")
            }
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "네트워크 오류 발생")
        }
    }
}
