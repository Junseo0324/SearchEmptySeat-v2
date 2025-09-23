package com.example.searchplacement.data.api

import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.reserve.ReservationRequest
import com.example.searchplacement.data.reserve.ReservationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ReservationApiService {
    //예약
    @POST("/api/reservations/create")
    suspend fun createReservation(
        @Header("Authorization") token: String,
        @Body request: ReservationRequest
    ): Response<ApiResponse<ReservationResponse>>

    @DELETE("/api/reservations/cancel/{reservationId}")
    suspend fun cancelReservation(
        @Header("Authorization") token: String,
        @Path("reservationId") reservationId: Long
    ): Response<ApiResponse<String>>

    @GET("/api/reservations/owner/{storeId}")
    suspend fun getOwnerReservations(
        @Header("Authorization") token: String,
        @Path("storeId") storeId: Long
    ): Response<ApiResponse<List<ReservationResponse>>>

    @GET("/api/reservations/user")
    suspend fun getUserReservations(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<ReservationResponse>>>

    @GET("/api/reservations/details/{reservationId}")
    suspend fun getReservationDetails(
        @Header("Authorization") token: String,
        @Path("reservationId") reservationId: Long
    ): Response<ApiResponse<ReservationResponse>>
}