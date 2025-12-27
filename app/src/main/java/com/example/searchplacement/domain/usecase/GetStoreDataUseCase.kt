package com.example.searchplacement.domain.usecase

import com.example.searchplacement.core.util.Result
import com.example.searchplacement.data.store.StoreResponse
import com.example.searchplacement.domain.repository.StoreRepository
import javax.inject.Inject

class GetStoreDataUseCase @Inject constructor(
    private val storeRepository: StoreRepository
) {
    suspend fun execute(storeId: Long): Result<StoreResponse, String> {
        return try {
            val response = storeRepository.getStoreData(storeId)
            if (response.status == "success" && response.data != null) {
                Result.Success(response.data)
            } else {
                Result.Error(response.message ?: "매장 정보를 불러오는데 실패했습니다.")
            }
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "네트워크 오류 발생")
        }
    }
}
