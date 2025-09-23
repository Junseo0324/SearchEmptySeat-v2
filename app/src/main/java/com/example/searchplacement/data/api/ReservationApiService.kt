package com.example.searchplacement.data.api

import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.reserve.ReservationRequest
import com.example.searchplacement.data.reserve.ReservationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReservationApiService {
    //예약
    @POST("/api/reservations/create")
    suspend fun createReservation(
        @Body request: ReservationRequest
    ): Response<ApiResponse<ReservationResponse>>

    @DELETE("/api/reservations/cancel/{reservationId}")
    suspend fun cancelReservation(
        @Path("reservationId") reservationId: Long
    ): Response<ApiResponse<String>>

    @GET("/api/reservations/owner/{storeId}")
    suspend fun getOwnerReservations(
        @Path("storeId") storeId: Long
    ): Response<ApiResponse<List<ReservationResponse>>>

    @GET("/api/reservations/user")
    suspend fun getUserReservations(
    ): Response<ApiResponse<List<ReservationResponse>>>

    @GET("/api/reservations/details/{reservationId}")
    suspend fun getReservationDetails(
        @Path("reservationId") reservationId: Long
    ): Response<ApiResponse<ReservationResponse>>
}