package com.example.searchplacement.domain.usecase

import com.example.searchplacement.core.util.Result
import com.example.searchplacement.data.mapper.toModel
import com.example.searchplacement.domain.model.StoreModel
import com.example.searchplacement.domain.repository.StoreRepository
import javax.inject.Inject

class SearchStoresUseCase @Inject constructor(
    private val repository: StoreRepository
) {
    suspend fun execute(query: String): Result<List<StoreModel>, String> {
        return try {
            val response = repository.searchStoresByName(query)
            if (response.status == "success" && response.data != null) {
                Result.Success(response.data.map { it.toModel() })
            } else {
                Result.Error(response.message)
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error occurred")
        }
    }
}
