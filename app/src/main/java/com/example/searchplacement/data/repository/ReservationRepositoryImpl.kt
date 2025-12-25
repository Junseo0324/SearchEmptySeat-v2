package com.example.searchplacement.data.repository

import com.example.searchplacement.data.api.ReservationApiService
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.reserve.ReservationRequest
import com.example.searchplacement.data.reserve.ReservationResponse
import com.example.searchplacement.domain.repository.ReservationRepository
import javax.inject.Inject

class ReservationRepositoryImpl @Inject constructor(
    private val apiService: ReservationApiService
) : ReservationRepository {
    override suspend fun createReservation(request: ReservationRequest): ApiResponse<ReservationResponse> {
        return try {
             val response = apiService.createReservation(request)
             if (response.isSuccessful && response.body() != null) {
                 response.body()!!
             } else {
                 ApiResponse("fail", "Failed to create reservation", null)
             }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun cancelReservation(reservationId: Long): ApiResponse<String> {
        return try {
             val response = apiService.cancelReservation(reservationId)
             if (response.isSuccessful && response.body() != null) {
                 response.body()!!
             } else {
                 ApiResponse("fail", "Failed to cancel reservation", null)
             }
        } catch (e: Exception) {
            ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun getOwnerReservations(storeId: Long): ApiResponse<List<ReservationResponse>> {
        return try {
             val response = apiService.getOwnerReservations(storeId)
             if (response.isSuccessful && response.body() != null) {
                 response.body()!!
             } else {
                 ApiResponse("fail", "Failed to get owner reservations", null)
             }
        } catch (e: Exception) {
             ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun getUserReservations(): ApiResponse<List<ReservationResponse>> {
        return try {
             val response = apiService.getUserReservations()
             if (response.isSuccessful && response.body() != null) {
                 response.body()!!
             } else {
                 ApiResponse("fail", "Failed to get user reservations", null)
             }
        } catch (e: Exception) {
             ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }

    override suspend fun getReservationDetails(reservationId: Long): ApiResponse<ReservationResponse> {
        return try {
             val response = apiService.getReservationDetails(reservationId)
             if (response.isSuccessful && response.body() != null) {
                 response.body()!!
             } else {
                 ApiResponse("fail", "Failed to get reservation details", null)
             }
        } catch (e: Exception) {
             ApiResponse("error", e.message ?: "Unknown error", null)
        }
    }
}
