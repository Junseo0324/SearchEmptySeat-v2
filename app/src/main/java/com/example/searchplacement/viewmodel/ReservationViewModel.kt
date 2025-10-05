package com.example.searchplacement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.data.reserve.ReservationRequest
import com.example.searchplacement.data.reserve.ReservationResponse
import com.example.searchplacement.data.reserve.ReservationWithStore
import com.example.searchplacement.repository.ReservationRepository
import com.example.searchplacement.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ReservationViewModel @Inject constructor(
    private val reservationRepository: ReservationRepository,
    private val storeRepository: StoreRepository
) : ViewModel() {

    private val _reservations = MutableStateFlow<List<ReservationResponse>>(emptyList())
    val reservations: StateFlow<List<ReservationResponse>> = _reservations

    private val _reservationsWithStore = MutableStateFlow<List<ReservationWithStore>>(emptyList())
    val reservationsWithStore: StateFlow<List<ReservationWithStore>> = _reservationsWithStore


    private val _reservationDetail = MutableStateFlow<ReservationResponse?>(null)
    val reservationDetail: StateFlow<ReservationResponse?> = _reservationDetail

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage

    val userId = MutableStateFlow<Long>(0L)

    fun createReservation(request: ReservationRequest, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val res = reservationRepository.createReservation(request)
            _statusMessage.value = res.body()?.message ?: "예약 생성 실패"
            onComplete(res.isSuccessful)
        }
    }

    fun cancelReservation(reservationId: Long, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val res = reservationRepository.cancelReservation(reservationId)
            _statusMessage.value = res.body()?.message ?: "예약 취소 실패"
            onComplete(res.isSuccessful)
        }
    }


    fun fetchOwnerReservations(storeId: Long) {
        viewModelScope.launch {
            val res = reservationRepository.getOwnerReservations(storeId)
            if (res.isSuccessful) {
                _reservations.value = res.body()?.data ?: emptyList()
            } else {
                _statusMessage.value = res.body()?.message ?: "예약 조회 실패"
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
                _statusMessage.value = res.body()?.message ?: "예약 조회 실패"
            }
        }
    }

    fun fetchReservationDetail(reservationId: Long) {
        viewModelScope.launch {
            val res = reservationRepository.getReservationDetails(reservationId)
            if (res.isSuccessful) {
                _reservationDetail.value = res.body()?.data
            } else {
                _statusMessage.value = res.body()?.message ?: "상세 조회 실패"
            }
        }
    }

    fun clearStatusMessage() {
        _statusMessage.value = null
    }
}
