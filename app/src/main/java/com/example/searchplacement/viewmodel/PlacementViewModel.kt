package com.example.searchplacement.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.placement.PlacementRequest
import com.example.searchplacement.data.placement.PlacementResponse
import com.example.searchplacement.data.placement.PlacementUpdateRequest
import com.example.searchplacement.repository.PlacementRepository
import com.example.searchplacement.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlacementViewModel @Inject constructor(
    private val placementRepository: PlacementRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _placement = MutableStateFlow<ApiResponse<PlacementResponse>?>(null)
    val placement = _placement.asStateFlow()

    private val _placementPK = MutableStateFlow<Long?>(null)
    val placementPK: StateFlow<Long?> = _placementPK

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    var token = ""

    // 자리 배치 생성
    fun createPlacement(request: PlacementRequest) {
        viewModelScope.launch {
            val token = userRepository.getUser()?.token ?: ""
            try {
                val response = placementRepository.createPlacement(token, request)
                Log.d("PlacementViewModel", "response : ${response}")
                if (response.isSuccessful && response.body() != null) {
                    _placement.value = response.body()

                    _error.value = null
                } else {
                    _error.value = response.errorBody()?.string() ?: "자리배치 생성 실패"

                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    // 매장별 자리배치 조회
    fun getPlacementByStore(storePK: Long) {
        viewModelScope.launch {
            token = userRepository.getUser()?.token ?: ""
            try {
                val response = placementRepository.getPlacementByStore(token, storePK)
                if (response.isSuccessful && response.body() != null) {
                    _placement.value = response.body()

                    _placementPK.value = response.body()?.data?.placementPK
                    _error.value = null
                } else {
                    _error.value = response.errorBody()?.string() ?: "자리배치 조회 실패"

                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    // 자리배치 상태/구조 업데이트
    fun updatePlacement(placementPK: Long, request: PlacementUpdateRequest) {
        viewModelScope.launch {
            val token = userRepository.getUser()?.token ?: ""
            try {
                val response = placementRepository.updatePlacement(token, placementPK, request)
                if (response.isSuccessful && response.body() != null) {
                    _placement.value = response.body()
                    Log.d("PlacementViewModel", "updatePlacement: 자리배치 성공 ${response.body()}")
                    Log.d("PlacementViewModel", "updatePlacement: 자리배치 성공 ${response.code()}")
                    Log.d("PlacementViewModel", "updatePlacement: 자리배치 성공 ${response.body()}")
                    _error.value = null
                } else {
                    _error.value = response.errorBody()?.string() ?: "자리배치 업데이트 실패"
                    Log.d("PlacementViewModel", "updatePlacement: 자리배치 실패 ${_error.value}")
                    Log.d("PlacementViewModel", "updatePlacement: 자리배치 성공 ${response.code()}")
                }
            } catch (e: Exception) {
                _error.value = e.message
                Log.d("PlacementViewModel", "updatePlacement: 자리배치 catch ${e.message}")

            }
        }
    }
}