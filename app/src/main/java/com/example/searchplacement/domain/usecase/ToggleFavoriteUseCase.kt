package com.example.searchplacement.domain.usecase

import com.example.searchplacement.core.util.Result
import com.example.searchplacement.domain.repository.FavoriteRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend fun execute(storeId: Long, isFavorite: Boolean): Result<Unit, String> {
        return try {
            if (isFavorite) {
                // If currently favorite, we want to remove it
                favoriteRepository.removeFavorite(storeId)
            } else {
                // If not favorite, we want to add it
                favoriteRepository.addFavorite(storeId)
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "즐겨찾기 변경 실패")
        }
    }
}
