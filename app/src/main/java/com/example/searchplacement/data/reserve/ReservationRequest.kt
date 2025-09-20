package com.example.searchplacement.data.reserve

data class ReservationRequest(
    val userId: Long,
    val storePK: Long,
    val reservationTime: String,
    val tableNumber: Int,
    val menu: Map<String, Any>,
    val partySize: Int,
    val paymentMethod: String,
    val status: String
)
