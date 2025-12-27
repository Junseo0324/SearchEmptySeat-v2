package com.example.searchplacement.presentation.user.home

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.core.util.Result
import com.example.searchplacement.data.dto.login.LoginRequest
import com.example.searchplacement.data.dto.login.LoginResponse
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.domain.repository.AuthRepository
import com.example.searchplacement.domain.repository.UserRepository
import com.example.searchplacement.domain.usecase.GetMapPinDetailUseCase
import com.example.searchplacement.domain.usecase.GetMapPinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val getMapPinsUseCase: GetMapPinsUseCase,
    private val getMapPinDetailUseCase: GetMapPinDetailUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    val user = _state.map { it.user }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _event = MutableSharedFlow<HomeEvent>()
    val event = _event.asSharedFlow()

    private val _loginResult = MutableStateFlow<ApiResponse<LoginResponse>?>(null)
    val loginResult = _loginResult.asStateFlow()

    private val _passwordUpdateResult = MutableStateFlow<ApiResponse<String>?>(null)
    val passwordUpdateResult = _passwordUpdateResult.asStateFlow()

    init {
        getUserData()
        loadMapPins()
    }

    fun onAction(action: HomeAction) {
        when (action) {

            is HomeAction.OnMarkerClick -> loadMapPinDetail(action.storeId)
            is HomeAction.OnStoreDetailClick -> {
                viewModelScope.launch {
                    _event.emit(HomeEvent.NavigateToStoreDetail(action.storeId))
                }
            }
        }
    }

    private fun loadMapPins() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = getMapPinsUseCase.execute()) {
                is Result.Success -> {
                    val mapPinModels = result.data
                    val geocoder = Geocoder(context, Locale.KOREA)
                    
                    val uiModels = withContext(Dispatchers.IO) {
                        mapPinModels.mapNotNull { pin ->
                            try {
                                val addr = geocoder.getFromLocationName(pin.location, 1)
                                if (!addr.isNullOrEmpty()) {
                                    MapPinUi(
                                        storePK = pin.storePK,
                                        lat = addr[0].latitude,
                                        lng = addr[0].longitude
                                    )
                                } else {
                                    null
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                null
                            }
                        }
                    }
                    _state.update { it.copy(isLoading = false, mapPins = uiModels) }
                }
                is Result.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.emit(HomeEvent.ShowSnackbar(result.error))
                }
            }
        }
    }

    private fun loadMapPinDetail(storeId: Long) {
        viewModelScope.launch {
            when (val result = getMapPinDetailUseCase.execute(storeId)) {
                is Result.Success -> {
                    _state.update { it.copy(selectedPinDetail = result.data) }
                }
                is Result.Error -> {
                    _event.emit(HomeEvent.ShowSnackbar(result.error))
                }
            }
        }
    }

    private fun getUserData() {
        viewModelScope.launch {
            val user = userRepository.getUser()
            _state.update { it.copy(user = user) }
        }
    }

    // --- Existing Auth Logic Below (Preserved for compatibility) ---

    fun authPassword(email: String, password: String) {
        viewModelScope.launch {
            val response = authRepository.login(LoginRequest(email, password))
            if (response.status == "success" && response != null) {
                _loginResult.value = response
            } else {
                _loginResult.value = ApiResponse(
                    status = "fail",
                    message = "Auth failed",
                    data = null
                )
            }
        }
    }


    fun updatePassword(userId: Long, newPassword: String) {
        viewModelScope.launch {
            try {
                val response = authRepository.updatePassword(userId, newPassword)
                if (response.status == "success") {
                    _passwordUpdateResult.value = response
                } else {
                    _passwordUpdateResult.value = ApiResponse(
                        status = "fail",
                        message = response.message,
                        data = null
                    )
                }
            } catch (e: Exception) {
                _passwordUpdateResult.value = ApiResponse(
                    status = "fail",
                    message = "네트워크 오류: ${e.message}",
                    data = null
                )
            }
        }
    }
}