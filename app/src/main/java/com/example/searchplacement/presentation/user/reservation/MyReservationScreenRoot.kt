package com.example.searchplacement.presentation.user.reservation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.searchplacement.presentation.user.review.ReviewBottomSheet

@Composable
fun MyReservationScreenRoot(
    onNavigateToStoreDetail: (Long) -> Unit,
    viewModel: MyReservationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                is MyReservationEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                MyReservationEvent.ReviewSubmitted -> {
                    // Possible navigation or additional UI feedback
                }
            }
        }
    }

    ReserveScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigateToStoreDetail = onNavigateToStoreDetail
    )

    if (state.showReviewBottomSheet && state.selectedReservation != null) {
        ReviewBottomSheet(
            reservation = state.selectedReservation!!,
            store = state.selectedStore,
            onDismiss = { viewModel.onAction(MyReservationAction.OnReviewDismiss) },
            onSubmit = { rating, reviewText, images ->
                viewModel.onAction(MyReservationAction.OnSubmitReview(rating.toFloat(), reviewText, images))
            }
        )
    }
}
