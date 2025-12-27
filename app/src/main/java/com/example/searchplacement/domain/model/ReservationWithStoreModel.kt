package com.example.searchplacement.domain.model

data class ReservationWithStoreModel(
    val reservation: ReservationModel,
    val store: StoreModel?
)
