package com.example.searchplacement.viewmodel.owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.data.reserve.ReservationResponse
import com.example.searchplacement.data.store.StoreResponse
import com.example.searchplacement.repository.ReservationRepository
import com.example.searchplacement.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnerReservationViewModel @Inject constructor(
    private val reservationRepository: ReservationRepository,
    private val storeRepository: StoreRepository
) : ViewModel() {

    private val _reservations = MutableStateFlow<List<ReservationResponse>>(emptyList())
    val reservations: StateFlow<List<ReservationResponse>> = _reservations

    private val _store = MutableStateFlow<StoreResponse?>(null)
    val store: StateFlow<StoreResponse?> = _store.asStateFlow()

    fun fetchOwnerReservationsAndStore(storeId: Long) {
        viewModelScope.launch {
            val reservationRes = reservationRepository.getOwnerReservations(storeId)
            val storeRes = storeRepository.getStoreData(storeId)
            if (reservationRes.isSuccessful && storeRes.isSuccessful) {
                _reservations.value = reservationRes.body()?.data ?: emptyList()
                _store.value = storeRes.body()?.data
            } else {
            }
        }
    }

}