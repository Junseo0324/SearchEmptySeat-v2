package com.example.searchplacement.domain.usecase

import com.example.searchplacement.core.util.Result
import com.example.searchplacement.data.mapper.toModel
import com.example.searchplacement.domain.model.FavoriteModel
import com.example.searchplacement.domain.repository.FavoriteRepository
import com.example.searchplacement.domain.repository.UserRepository
import javax.inject.Inject

class GetFavoriteListUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val userRepository: UserRepository
) {
    suspend fun execute(): Result<List<FavoriteModel>, String> {
        return try {
            val user = userRepository.getUser()
            val userId = user?.userId
            
            if (userId != null) {
                val response = favoriteRepository.getFavoriteList(userId)
                if (response.status == "success" && response.data != null) {
                    Result.Success(response.data.map { it.toModel() })
                } else {
                    Result.Error(response.message ?: "즐겨찾기 목록을 불러오는데 실패했습니다.")
                }
            } else {
                Result.Error("로그인이 필요합니다.")
            }
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "네트워크 오류 발생")
        }
    }
}
