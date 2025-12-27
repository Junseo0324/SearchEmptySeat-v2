package com.example.searchplacement.domain.model

data class ReservationModel(
    val reservationPK: Long,
    val userId: Long,
    val storePK: Long,
    val reservationNum: Int,
    val reservationTime: String,
    val tableNumber: Int,
    val menu: Map<String, Any>,
    val seats: String,
    val partySize: Int,
    val paymentMethod: String,
    val status: String,
    val createdDate: String,
    val endDate: String?
)
