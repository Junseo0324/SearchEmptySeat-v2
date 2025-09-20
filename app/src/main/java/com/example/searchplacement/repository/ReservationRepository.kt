package com.example.searchplacement.repository

import com.example.searchplacement.data.api.APIService
import com.example.searchplacement.data.reserve.ReservationRequest
import javax.inject.Inject

class ReservationRepository @Inject constructor(
    private val apiService: APIService
) {
    suspend fun createReservation(token: String, request: ReservationRequest) =
        apiService.createReservation("Bearer $token", request)

    suspend fun cancelReservation(token: String, reservationId: Long) =
        apiService.cancelReservation("Bearer $token", reservationId)

    suspend fun getOwnerReservations(token: String, storeId: Long) =
        apiService.getOwnerReservations("Bearer $token", storeId)

    suspend fun getUserReservations(token: String) =
        apiService.getUserReservations("Bearer $token")

    suspend fun getReservationDetails(token: String, reservationId: Long) =
        apiService.getReservationDetails("Bearer $token", reservationId)
}
