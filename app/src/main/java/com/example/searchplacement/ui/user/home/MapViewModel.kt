package com.example.searchplacement.ui.user.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.data.map.MapPinDetailResponse
import com.example.searchplacement.data.map.MapPinResponse
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.repository.MapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapRepository: MapRepository
) : ViewModel() {

    private val _mapPins = MutableStateFlow<ApiResponse<List<MapPinResponse>>?>(null)
    val mapPins: StateFlow<ApiResponse<List<MapPinResponse>>?> = _mapPins.asStateFlow()

    private val _mapPinDetail = MutableStateFlow<ApiResponse<MapPinDetailResponse>?>(null)
    val mapPinDetail: StateFlow<ApiResponse<MapPinDetailResponse>?> = _mapPinDetail.asStateFlow()

    fun loadMapPins() {
        viewModelScope.launch {
            try {
                val response: Response<ApiResponse<List<MapPinResponse>>> = mapRepository.getMapPins()
                if (response.isSuccessful && response.body() != null) {
                    _mapPins.value = response.body()
                } else {
                    _mapPins.value = ApiResponse(
                        status = "fail",
                        message = response.errorBody()?.string() ?: "지도 핀 불러오기 실패",
                        data = emptyList()

                    )
                }
            } catch (e: Exception) {
                _mapPins.value = ApiResponse(
                    status = "fail",
                    message = "네트워크 오류: ${e.message}",
                    data = emptyList()
                )
            }
        }
    }

    fun loadMapPinDetail(storePK: Long) {
        viewModelScope.launch {
            try {
                val response = mapRepository.getMapPinDetail(storePK)
                if (response.isSuccessful && response.body() != null) {
                    _mapPinDetail.value = response.body()
                } else {
                    _mapPinDetail.value = ApiResponse(
                        status = "fail",
                        message = response.errorBody()?.string() ?: "지도 핀 상세 불러오기 실패",
                        data = null
                    )
                }
            } catch (e: Exception) {
                _mapPinDetail.value = ApiResponse(
                    status = "fail",
                    message = "네트워크 오류: ${e.message}",
                    data = null
                )
            }
        }
    }
}
