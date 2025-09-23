package com.example.searchplacement.repository

import com.example.searchplacement.data.api.ReservationApiService
import com.example.searchplacement.data.reserve.ReservationRequest
import javax.inject.Inject

class ReservationRepository @Inject constructor(
    private val apiService: ReservationApiService
) {
    suspend fun createReservation(request: ReservationRequest) =
        apiService.createReservation(request)

    suspend fun cancelReservation(reservationId: Long) =
        apiService.cancelReservation(reservationId)

    suspend fun getOwnerReservations(storeId: Long) =
        apiService.getOwnerReservations(storeId)

    suspend fun getUserReservations() =
        apiService.getUserReservations()

    suspend fun getReservationDetails(reservationId: Long) =
        apiService.getReservationDetails(reservationId)
}
