package com.example.searchplacement.data.reserve

import com.example.searchplacement.data.store.StoreResponse

data class ReservationWithStore(
    val reservation: ReservationResponse,
    val store: StoreResponse?
)