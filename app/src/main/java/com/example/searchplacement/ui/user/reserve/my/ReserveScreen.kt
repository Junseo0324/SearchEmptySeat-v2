package com.example.searchplacement.ui.user.reserve.my

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.searchplacement.data.reserve.ReservationResponse
import com.example.searchplacement.data.store.StoreResponse
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.ButtonMainColor
import com.example.searchplacement.ui.theme.CategoryBgColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.Gray
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.user.review.ReviewBottomSheet
import com.example.searchplacement.ui.utils.isCancellable
import com.example.searchplacement.viewmodel.ReservationViewModel
import com.example.searchplacement.viewmodel.ReviewViewModel

@Composable
fun ReserveScreen(navController: NavHostController) {

    val reservationViewModel: ReservationViewModel = hiltViewModel()
    val reviewViewModel: ReviewViewModel = hiltViewModel()

    val context = LocalContext.current
    val reservationsWithStore by reservationViewModel.reservationsWithStore.collectAsState()

    val upcomingReservations = remember(reservationsWithStore) {
        reservationsWithStore.filter { it.reservation.status == "pending" }
    }
    val completedReservations = remember(reservationsWithStore) {
        reservationsWithStore.filter { it.reservation.status == "completed" }
    }
    var selectedTab by remember { mutableStateOf(0) }

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedReservation by remember { mutableStateOf<ReservationResponse?>(null) }
    var selectedStore by remember { mutableStateOf<StoreResponse?>(null) }

    LaunchedEffect(Unit) {
        reservationViewModel.fetchUserReservations()
    }

    val tabs = listOf(
        "예약 중 (${upcomingReservations.size})",
        "방문 완료 (${completedReservations.size})"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CategoryBgColor)
    ) {

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = White,
            shadowElevation = Dimens.Nano
        ) {
            Column {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = White,
                    contentColor = ButtonMainColor,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            height = 3.dp,
                            color = ButtonMainColor
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            modifier = Modifier.height(48.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (index == 0)
                                        Icons.Default.Schedule else Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = if (selectedTab == index)
                                        ButtonMainColor else Gray
                                )
                                Text(
                                    text = title,
                                    fontSize = 15.sp,
                                    fontWeight = if (selectedTab == index)
                                        FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == index)
                                        ButtonMainColor else Gray
                                )
                            }
                        }
                    }
                }
            }
        }

        val currentList = if (selectedTab == 0) upcomingReservations else completedReservations

        if (currentList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (selectedTab == 0) "현재 예약 중인 가게가 없습니다." else "방문 완료한 예약이 없습니다.",
                    style = AppTextStyle.Body.copy(color = Gray),
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(White),
                contentPadding = PaddingValues(Dimens.Medium),
                verticalArrangement = Arrangement.spacedBy(Dimens.Medium)
            ) {
                items(currentList.size) { index ->
                    val item = currentList[index]
                    ReservedList(
                        navController = navController,
                        reservation = item.reservation,
                        store = item.store,
                        onReviewClick = { reservation, store ->
                            selectedReservation = reservation
                            selectedStore = store
                            showBottomSheet = true
                        },
                        onCancelClick = { reservationId ->
                            val reservationTime = item.reservation.reservationTime
                            if (isCancellable(reservationTime)) {
                                reservationViewModel.cancelReservation(reservationId) { success ->
                                    if (success) {
                                        Toast.makeText(context, "예약이 취소되었습니다.", Toast.LENGTH_SHORT).show()
                                        reservationViewModel.fetchUserReservations()
                                    } else {
                                        Toast.makeText(context, "예약 취소에 실패했습니다.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(context, "예약 30분 전에는 취소할 수 없습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            }
        }
    }

    if (showBottomSheet && selectedReservation != null) {
        ReviewBottomSheet(
            reservation = selectedReservation!!,
            store = selectedStore,
            onDismiss = { showBottomSheet = false },
            onSubmit = { rating, reviewText, images ->
                reviewViewModel.submitReview(
                    context = context,
                    storePK = selectedStore?.storePK ?: 0,
                    rating = rating.toFloat(),
                    content = reviewText,
                    imageUris = images,
                    onSuccess = {
                        showBottomSheet = false
                        reviewViewModel.clearReviewSubmitResult()
                        reservationViewModel.fetchUserReservations()
                        Toast.makeText(context,"리뷰가 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    },
                    onError = { errorMessage ->
                        showBottomSheet = false
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                )
                showBottomSheet = false
            }
        )
    }
}

