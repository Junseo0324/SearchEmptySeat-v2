package com.example.searchplacement.presentation.user.reservation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.core.util.Result
import com.example.searchplacement.domain.usecase.CancelReservationUseCase
import com.example.searchplacement.domain.usecase.GetUserReservationsUseCase
import com.example.searchplacement.domain.usecase.RegisterReviewUseCase
import com.example.searchplacement.presentation.utils.isCancellable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyReservationViewModel @Inject constructor(
    private val getUserReservationsUseCase: GetUserReservationsUseCase,
    private val cancelReservationUseCase: CancelReservationUseCase,
    private val registerReviewUseCase: RegisterReviewUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MyReservationState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<MyReservationEvent>()
    val event = _event.asSharedFlow()

    init {
        fetchUserReservations()
    }

    fun onAction(action: MyReservationAction) {
        when (action) {
            MyReservationAction.OnFetchReservations -> fetchUserReservations()
            is MyReservationAction.OnTabSelected -> {
                _state.update { it.copy(selectedTab = action.index) }
            }

            is MyReservationAction.OnCancelReservation -> {
                cancelReservation(action.reservationId, action.reservationTime)
            }

            is MyReservationAction.OnReviewClick -> {
                _state.update {
                    it.copy(
                        showReviewBottomSheet = true,
                        selectedReservation = action.reservation,
                        selectedStore = action.store
                    )
                }
            }

            MyReservationAction.OnReviewDismiss -> {
                _state.update {
                    it.copy(
                        showReviewBottomSheet = false,
                        selectedReservation = null,
                        selectedStore = null
                    )
                }
            }

            is MyReservationAction.OnSubmitReview -> {
                submitReview(action.rating, action.content, action.imageUris)
            }
        }
    }

    private fun fetchUserReservations() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = getUserReservationsUseCase.execute()) {
                is Result.Success -> {
                    val combinedList = result.data ?: emptyList()
                    val upcoming = combinedList.filter { it.reservation.status == "pending" }
                    val completed = combinedList.filter { it.reservation.status == "completed" }
                    val tabs = listOf(
                        "예약 중 (${upcoming.size})",
                        "방문 완료 (${completed.size})"
                    )

                    _state.update {
                        it.copy(
                            reservationsWithStore = combinedList,
                            upcomingReservations = upcoming,
                            completedReservations = completed,
                            tabs = tabs,
                            isLoading = false,
                            error = null
                        )
                    }
                }

                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.error ?: "예약 목록을 불러오는데 실패했습니다."
                        )
                    }
                }
            }
        }
    }

    private fun cancelReservation(reservationId: Long, reservationTime: String) {
        if (!isCancellable(reservationTime)) {
            viewModelScope.launch {
                _event.emit(MyReservationEvent.ShowToast("예약 30분 전에는 취소할 수 없습니다."))
            }
            return
        }

        viewModelScope.launch {
            when (val result = cancelReservationUseCase.execute(reservationId)) {
                is Result.Success -> {
                    _event.emit(MyReservationEvent.ShowToast("예약이 취소되었습니다."))
                    fetchUserReservations()
                }

                is Result.Error -> {
                    _event.emit(MyReservationEvent.ShowToast(result.error ?: "예약 취소에 실패했습니다."))
                }
            }
        }
    }

    private fun submitReview(rating: Float, content: String, imageUris: List<Uri>) {
        val storePK = _state.value.selectedStore?.storePK ?: return

        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }

                when (val result = registerReviewUseCase.execute(storePK, rating, content, imageUris)) {
                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                showReviewBottomSheet = false,
                                selectedReservation = null,
                                selectedStore = null
                            )
                        }
                        _event.emit(MyReservationEvent.ShowToast("리뷰가 등록되었습니다."))
                        _event.emit(MyReservationEvent.ReviewSubmitted)
                        fetchUserReservations()
                    }

                    is Result.Error -> {
                        _state.update { it.copy(isLoading = false) }
                        _event.emit(MyReservationEvent.ShowToast(result.error ?: "리뷰 등록 실패"))
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                _event.emit(MyReservationEvent.ShowToast("리뷰 등록 중 오류 발생: ${e.localizedMessage}"))
            }
        }
    }
}
