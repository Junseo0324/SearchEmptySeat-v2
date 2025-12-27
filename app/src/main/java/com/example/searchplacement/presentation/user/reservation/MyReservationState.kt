package com.example.searchplacement.presentation.user.reservation

import com.example.searchplacement.data.reserve.ReservationResponse
import com.example.searchplacement.data.reserve.ReservationWithStore
import com.example.searchplacement.data.store.StoreResponse

data class MyReservationState(
    val reservationsWithStore: List<ReservationWithStore> = emptyList(),
    val upcomingReservations: List<ReservationWithStore> = emptyList(),
    val completedReservations: List<ReservationWithStore> = emptyList(),
    val tabs: List<String> = listOf("예약 중 (0)", "방문 완료 (0)"),
    val selectedTab: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showReviewBottomSheet: Boolean = false,
    val selectedReservation: ReservationResponse? = null,
    val selectedStore: StoreResponse? = null
)
