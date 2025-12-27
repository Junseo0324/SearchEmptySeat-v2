package com.example.searchplacement.presentation.user.reservation

import com.example.searchplacement.domain.model.ReservationModel
import com.example.searchplacement.domain.model.ReservationWithStoreModel
import com.example.searchplacement.domain.model.StoreModel

data class MyReservationState(
    val reservationsWithStore: List<ReservationWithStoreModel> = emptyList(),
    val upcomingReservations: List<ReservationWithStoreModel> = emptyList(),
    val completedReservations: List<ReservationWithStoreModel> = emptyList(),
    val tabs: List<String> = listOf("예약 중 (0)", "방문 완료 (0)"),
    val selectedTab: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showReviewBottomSheet: Boolean = false,
    val selectedReservation: ReservationModel? = null,
    val selectedStore: StoreModel? = null
)
