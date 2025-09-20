package com.example.searchplacement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.data.reserve.ReservationRequest
import com.example.searchplacement.data.reserve.ReservationResponse
import com.example.searchplacement.repository.ReservationRepository
import com.example.searchplacement.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ReservationViewModel @Inject constructor(
    private val reservationRepository: ReservationRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _reservations = MutableStateFlow<List<ReservationResponse>>(emptyList())
    val reservations: StateFlow<List<ReservationResponse>> = _reservations

    private val _reservationDetail = MutableStateFlow<ReservationResponse?>(null)
    val reservationDetail: StateFlow<ReservationResponse?> = _reservationDetail

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage

    val userId = MutableStateFlow<Long>(0L)

    fun createReservation(request: ReservationRequest, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val token = userRepository.getUser()?.token ?: ""
            val res = reservationRepository.createReservation(token, request)
            _statusMessage.value = res.body()?.message ?: "예약 생성 실패"
            onComplete(res.isSuccessful)
        }
    }

    fun cancelReservation(reservationId: Long, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val token = userRepository.getUser()?.token ?: ""
            val res = reservationRepository.cancelReservation(token, reservationId)
            _statusMessage.value = res.body()?.message ?: "예약 취소 실패"
            onComplete(res.isSuccessful)
        }
    }


    fun fetchOwnerReservations(storeId: Long) {
        viewModelScope.launch {
            val token = userRepository.getUser()?.token ?: ""
            val res = reservationRepository.getOwnerReservations(token, storeId)
            if (res.isSuccessful) {
                _reservations.value = res.body()?.data ?: emptyList()
            } else {
                _statusMessage.value = res.body()?.message ?: "예약 조회 실패"
            }
        }
    }

    fun getUserIdAndFetchReservations() {
        viewModelScope.launch {
            fetchUserReservations()
        }
    }


    fun fetchUserReservations() {
        viewModelScope.launch {
            val token = userRepository.getUser()?.token ?: ""
            val res = reservationRepository.getUserReservations(token)
            if (res.isSuccessful) {
                _reservations.value = res.body()?.data ?: emptyList()
            } else {
                _statusMessage.value = res.body()?.message ?: "예약 조회 실패"
            }
        }
    }

    fun fetchReservationDetail(reservationId: Long) {
        viewModelScope.launch {
            val token = userRepository.getUser()?.token ?: ""
            val res = reservationRepository.getReservationDetails(token, reservationId)
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
