package com.example.searchplacement.presentation.user.reservation

import android.net.Uri
import com.example.searchplacement.domain.model.ReservationModel
import com.example.searchplacement.domain.model.StoreModel

sealed interface MyReservationAction {
    data object OnFetchReservations : MyReservationAction
    data class OnTabSelected(val index: Int) : MyReservationAction
    data class OnCancelReservation(val reservationId: Long, val reservationTime: String) : MyReservationAction
    data class OnReviewClick(val reservation: ReservationModel, val store: StoreModel?) : MyReservationAction
    data object OnReviewDismiss : MyReservationAction
    data class OnSubmitReview(
        val rating: Float,
        val content: String,
        val imageUris: List<Uri>
    ) : MyReservationAction
}
