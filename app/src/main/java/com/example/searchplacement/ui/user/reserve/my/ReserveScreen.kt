package com.example.searchplacement.ui.user.reserve.my

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.ButtonMainColor
import com.example.searchplacement.ui.theme.CategoryBgColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.Gray
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.viewmodel.ReservationViewModel

@Composable
fun ReserveScreen(navController: NavHostController) {

    val reservationViewModel: ReservationViewModel = hiltViewModel()
    val reservationsWithStore by reservationViewModel.reservationsWithStore.collectAsState()

    val upcomingReservations = remember(reservationsWithStore) {
        reservationsWithStore.filter { it.reservation.status == "pending" }
    }
    val completedReservations = remember(reservationsWithStore) {
        reservationsWithStore.filter { it.reservation.status == "completed" }
    }
    var selectedTab by remember { mutableStateOf(0) }


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
                        store = item.store
                    )
                }
            }
        }
    }
}

