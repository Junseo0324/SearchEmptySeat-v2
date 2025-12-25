package com.example.searchplacement.domain.repository

import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.reserve.ReservationRequest
import com.example.searchplacement.data.reserve.ReservationResponse

interface ReservationRepository {
    suspend fun createReservation(request: ReservationRequest): ApiResponse<ReservationResponse>
    suspend fun cancelReservation(reservationId: Long): ApiResponse<String>
    suspend fun getOwnerReservations(storeId: Long): ApiResponse<List<ReservationResponse>>
    suspend fun getUserReservations(): ApiResponse<List<ReservationResponse>>
    suspend fun getReservationDetails(reservationId: Long): ApiResponse<ReservationResponse>
}
