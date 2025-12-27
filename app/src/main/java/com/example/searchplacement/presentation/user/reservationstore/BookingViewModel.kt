package com.example.searchplacement.presentation.user.reservationstore

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.core.util.Result
import com.example.searchplacement.data.menu.MenuResponse
import com.example.searchplacement.data.placement.PlacementResponse
import com.example.searchplacement.data.reserve.ReservationData
import com.example.searchplacement.data.reserve.ReservationRequest
import com.example.searchplacement.data.section.MenuSectionResponse
import com.example.searchplacement.data.store.StoreResponse
import com.example.searchplacement.domain.usecase.CreateReservationUseCase
import com.example.searchplacement.domain.usecase.GetCurrentUserUseCase
import com.example.searchplacement.domain.usecase.GetMenuSectionsUseCase
import com.example.searchplacement.domain.usecase.GetMenusUseCase
import com.example.searchplacement.domain.usecase.GetPlacementUseCase
import com.example.searchplacement.domain.usecase.GetStoreDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val createReservationUseCase: CreateReservationUseCase,
    private val getStoreDataUseCase: GetStoreDataUseCase,
    private val getMenusUseCase: GetMenusUseCase,
    private val getMenuSectionsUseCase: GetMenuSectionsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getPlacementUseCase: GetPlacementUseCase
) : ViewModel() {

    private val _reservationData = mutableStateOf(ReservationData())
    val reservationData: State<ReservationData> = _reservationData

    private val _menus = MutableStateFlow<List<MenuResponse>>(emptyList())
    val menus: StateFlow<List<MenuResponse>> = _menus.asStateFlow()

    private val _sections = MutableStateFlow<List<MenuSectionResponse>>(emptyList())
    val sections: StateFlow<List<MenuSectionResponse>> = _sections.asStateFlow()

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId.asStateFlow()

    private val _placement = MutableStateFlow<PlacementResponse?>(null)
    val placement = _placement.asStateFlow()

    private val _storeData = MutableStateFlow<StoreResponse?>(null)
    val storeData: StateFlow<StoreResponse?> = _storeData.asStateFlow()

    fun updateReservation(block: (ReservationData) -> ReservationData) {
        _reservationData.value = block(_reservationData.value)
    }

    fun createReservation(request: ReservationRequest, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = createReservationUseCase.execute(request)
            onComplete(result is Result.Success)
        }
    }

    fun getPlacementByStore(storePK: Long) {
        viewModelScope.launch {
            when (val result = getPlacementUseCase.execute(storePK)) {
                is Result.Success -> {
                    _placement.value = result.data
                }
                is Result.Error -> {
                    // Handle error
                }
            }
        }
    }

    fun fetchMenusAndSections(storeId: Long) {
        viewModelScope.launch {
            _userId.value = getCurrentUserUseCase.execute()?.userId
            
            when (val menuResult = getMenusUseCase.execute(storeId)) {
                is Result.Success -> _menus.value = menuResult.data ?: emptyList()
                is Result.Error -> {}
            }
            
            when (val sectionResult = getMenuSectionsUseCase.execute(storeId)) {
                is Result.Success -> _sections.value = sectionResult.data ?: emptyList()
                is Result.Error -> {}
            }
        }
    }

    fun getStoreData(storeId: Long) {
        viewModelScope.launch {
            when (val result = getStoreDataUseCase.execute(storeId)) {
                is Result.Success -> {
                    _storeData.value = result.data
                }
                is Result.Error -> {
                    Log.d("BookingViewModel", "getStoreData: ${result.error}")
                }
            }
        }
    }
}
