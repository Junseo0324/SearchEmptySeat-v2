package com.example.searchplacement.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.data.menu.MenuResponse
import com.example.searchplacement.data.placement.PlacementResponse
import com.example.searchplacement.data.reserve.ReservationData
import com.example.searchplacement.data.reserve.ReservationRequest
import com.example.searchplacement.data.reserve.ReservationResponse
import com.example.searchplacement.data.reserve.ReservationWithStore
import com.example.searchplacement.data.section.MenuSectionResponse
import com.example.searchplacement.data.store.StoreResponse
import com.example.searchplacement.repository.MenuRepository
import com.example.searchplacement.repository.MenuSectionRepository
import com.example.searchplacement.repository.PlacementRepository
import com.example.searchplacement.repository.ReservationRepository
import com.example.searchplacement.repository.StoreRepository
import com.example.searchplacement.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ReservationViewModel @Inject constructor(
    private val reservationRepository: ReservationRepository,
    private val storeRepository: StoreRepository,
    private val menuRepository: MenuRepository,
    private val menuSectionRepository: MenuSectionRepository,
    private val userRepository: UserRepository,
    private val placementRepository: PlacementRepository
) : ViewModel() {

    private val _reservations = MutableStateFlow<List<ReservationResponse>>(emptyList())
    val reservations: StateFlow<List<ReservationResponse>> = _reservations

    private val _reservationsWithStore = MutableStateFlow<List<ReservationWithStore>>(emptyList())
    val reservationsWithStore: StateFlow<List<ReservationWithStore>> = _reservationsWithStore

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


    fun updateReservation(block: (ReservationData) -> ReservationData) {
        _reservationData.value = block(_reservationData.value)
    }

    private val _storeData = MutableStateFlow<StoreResponse?>(null)
    val storeData: StateFlow<StoreResponse?> = _storeData.asStateFlow()



    fun createReservation(request: ReservationRequest, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val res = reservationRepository.createReservation(request)
            onComplete(res.isSuccessful)
        }
    }

    fun cancelReservation(reservationId: Long, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val res = reservationRepository.cancelReservation(reservationId)
            onComplete(res.isSuccessful)
        }
    }

    fun getPlacementByStore(storePK: Long) {
        viewModelScope.launch {
            try {
                val response = placementRepository.getPlacementByStore(storePK)
                if (response.isSuccessful && response.body() != null) {
                    _placement.value = response.body()?.data
                } else {

                }
            } catch (e: Exception) {
            }
        }
    }

    fun fetchMenusAndSections(storeId: Long) {
        viewModelScope.launch {
            _userId.value = userRepository.getUser()?.userId
            val menuRes = menuRepository.getMenus(storeId)
            val sectionRes = menuSectionRepository.getSections(storeId)
            if (menuRes.isSuccessful) _menus.value = menuRes.body()?.data ?: emptyList()
            if (sectionRes.isSuccessful) _sections.value = sectionRes.body()?.data ?: emptyList()
        }
    }


    fun fetchOwnerReservations(storeId: Long) {
        viewModelScope.launch {
            val res = reservationRepository.getOwnerReservations(storeId)
            if (res.isSuccessful) {
                _reservations.value = res.body()?.data ?: emptyList()
            } else {
            }
        }
    }


    fun fetchUserReservations() {
        viewModelScope.launch {
            val res = reservationRepository.getUserReservations()
            if (res.isSuccessful) {
                val reservations = res.body()?.data ?: emptyList()

                val combinedList = reservations.map { reservation ->
                    val storeRes = try {
                        val response = storeRepository.getStoreData(reservation.storePK)
                        if (response.isSuccessful) response.body()?.data else null
                    } catch (e: Exception) {
                        null
                    }
                    ReservationWithStore(reservation, storeRes)
                }

                _reservationsWithStore.value = combinedList
            } else {
            }
        }
    }

    fun getStoreData(storeId: Long) {
        viewModelScope.launch {
            val response = storeRepository.getStoreData(storeId)
            if (response.isSuccessful && response.body()?.data != null) {
                _storeData.value = response.body()?.data
            }  else {
                Log.d("ReservationViewModel", "getStoreData: 실패")
            }
        }
    }
}
