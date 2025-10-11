package com.example.searchplacement.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.data.store.StoreResponse
import com.example.searchplacement.repository.FavoriteRepository
import com.example.searchplacement.repository.StoreRepository
import com.example.searchplacement.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val favoriteRepository: FavoriteRepository,
    private val storeRepository: StoreRepository
) : ViewModel() {

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _storeData = MutableStateFlow<StoreResponse?>(null)
    val storeData: StateFlow<StoreResponse?> = _storeData.asStateFlow()

    fun getStoreData(storeId: Long) {
        viewModelScope.launch {
            val response = storeRepository.getStoreData(storeId)
            if (response.isSuccessful && response.body()?.data != null) {
                _storeData.value = response.body()?.data
                checkFavorite(storeId)
            }  else {
                Log.d("TAG", "getStoreData: 실패")
            }
        }
    }

    fun checkFavorite(storeId: Long) {
        viewModelScope.launch {
            try {
                val userId = userRepository.getUser()?.userId ?: return@launch
                val favorites = favoriteRepository.getFavoriteList(userId)
                _isFavorite.value = favorites.body()?.data?.any { it.store.storePK == storeId } == true
            } catch (_: Exception) {
                _isFavorite.value = false
            }
        }
    }

    fun toggleFavorite(storeId: Long) {
        viewModelScope.launch {
            try {
                if (_isFavorite.value) {
                    favoriteRepository.removeFavorite(storeId)
                    _isFavorite.value = false
                } else {
                    favoriteRepository.addFavorite(storeId)
                    _isFavorite.value = true
                }
            } catch (_: Exception) {
                Log.d("TAG", "toggleFavorite: ${_isFavorite.value}로 변경")
            }
        }
    }


}