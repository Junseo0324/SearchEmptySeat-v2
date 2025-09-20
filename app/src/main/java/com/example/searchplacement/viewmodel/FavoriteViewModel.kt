package com.example.searchplacement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.store.FavoriteResponse
import com.example.searchplacement.repository.FavoriteRepository
import com.example.searchplacement.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _favoriteList = MutableStateFlow<ApiResponse<List<FavoriteResponse>>?>(null)
    val favoriteList: StateFlow<ApiResponse<List<FavoriteResponse>>?> = _favoriteList.asStateFlow()
    var token = ""
    fun getFavoriteList() {
        viewModelScope.launch {
            try {
                token = userRepository.getUser()?.token ?: ""
                val userId = userRepository.getUser()?.userId ?: ""
                val response = favoriteRepository.getFavoriteList(token, userId)

                if (response.isSuccessful && response.body() != null) {
                    _favoriteList.value = response.body()
                } else {
                    _favoriteList.value = ApiResponse(
                        status = "fail",
                        message = response.errorBody()?.string() ?: "찜 목록 불러오기 실패",
                        data = emptyList()
                    )
                }
            } catch (e: Exception) {
                _favoriteList.value = ApiResponse(
                    status = "fail",
                    message = "네트워크 오류: ${e.message}",
                    data = emptyList()
                )
            }
        }
    }


    fun addFavorite(storeId: Long) {
        viewModelScope.launch {
            try {
                val token = userRepository.getUser()?.token ?: ""
                favoriteRepository.addFavorite(token, storeId)
            } catch (_: Exception) {}
        }
    }

    fun removeFavorite(storeId: Long) {
        viewModelScope.launch {
            try {
                val token = userRepository.getUser()?.token ?: ""
                favoriteRepository.removeFavorite(token, storeId)
            } catch (_: Exception) {}
        }
    }
}
