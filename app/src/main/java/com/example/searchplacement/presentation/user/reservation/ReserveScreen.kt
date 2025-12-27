package com.example.searchplacement.presentation.user.reservation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.presentation.theme.AppTextStyle
import com.example.searchplacement.presentation.theme.ButtonMainColor
import com.example.searchplacement.presentation.theme.CategoryBgColor
import com.example.searchplacement.presentation.theme.Dimens
import com.example.searchplacement.presentation.theme.Gray
import com.example.searchplacement.presentation.theme.IconTextColor
import com.example.searchplacement.presentation.theme.White

@Composable
fun ReserveScreen(
    state: MyReservationState,
    onAction: (MyReservationAction) -> Unit,
    onNavigateToStoreDetail: (Long) -> Unit
) {

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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = Dimens.Medium),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = Color(0xFF27AE60),
                        modifier = Modifier.size(Dimens.Large)
                    )
                    Text(
                        text = "예약 현황",
                        style = AppTextStyle.Body.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = IconTextColor)
                    )
                }
                TabRow(
                    selectedTabIndex = state.selectedTab,
                    containerColor = White,
                    contentColor = ButtonMainColor,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[state.selectedTab]),
                            height = 3.dp,
                            color = ButtonMainColor
                        )
                    }
                ) {
                    state.tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = state.selectedTab == index,
                            onClick = { onAction(MyReservationAction.OnTabSelected(index)) },
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
                                    tint = if (state.selectedTab == index)
                                        ButtonMainColor else Gray
                                )
                                Text(
                                    text = title,
                                    fontSize = 15.sp,
                                    fontWeight = if (state.selectedTab == index)
                                        FontWeight.Bold else FontWeight.Normal,
                                    color = if (state.selectedTab == index)
                                        ButtonMainColor else Gray
                                )
                            }
                        }
                    }
                }
            }
        }

        val currentList = if (state.selectedTab == 0) state.upcomingReservations else state.completedReservations

        if (currentList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (state.selectedTab == 0) "현재 예약 중인 가게가 없습니다." else "방문 완료한 예약이 없습니다.",
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
                        onNavigateToStoreDetail = onNavigateToStoreDetail,
                        reservation = item.reservation,
                        store = item.store,
                        onReviewClick = { reservation, store ->
                            onAction(MyReservationAction.OnReviewClick(reservation, store))
                        },
                        onCancelClick = { reservationId ->
                            onAction(MyReservationAction.OnCancelReservation(reservationId, item.reservation.reservationTime))
                        }
                    )
                }
            }
        }
    }
}

