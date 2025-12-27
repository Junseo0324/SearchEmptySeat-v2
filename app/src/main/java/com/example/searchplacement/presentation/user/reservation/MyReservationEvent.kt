package com.example.searchplacement.presentation.user.reservation

sealed interface MyReservationEvent {
    data class ShowToast(val message: String) : MyReservationEvent
    data object ReviewSubmitted : MyReservationEvent
}
