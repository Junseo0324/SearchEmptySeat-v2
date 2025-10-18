package com.example.searchplacement.ui.owner.reservation

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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.ButtonMainColor
import com.example.searchplacement.ui.theme.CategoryBgColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.Gray
import com.example.searchplacement.ui.theme.IconTextColor
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.theme.isOpenColor
import com.example.searchplacement.viewmodel.owner.OwnerReservationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerReservationScreen(storeId: Long) {
    val reservationViewModel: OwnerReservationViewModel = hiltViewModel()

    var selectedTab by remember { mutableStateOf(0) }

    val reservations by reservationViewModel.reservations.collectAsState()
    val store by reservationViewModel.store.collectAsState()

    val upcomingReservations = remember(reservations) {
        reservations.filter { it.status == "pending" }
    }
    val completedReservations = remember(reservations) {
        reservations.filter { it.status == "completed" || it.status == "cancelled" }
    }
    val tabs = listOf(
        "예약 중 (${upcomingReservations.size})",
        "방문 완료 (${completedReservations.size})"
    )
    LaunchedEffect(Unit) {
        reservationViewModel.fetchOwnerReservationsAndStore(storeId)
    }

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
                        tint = isOpenColor,
                        modifier = Modifier.size(Dimens.Large)
                    )
                    Text(
                        text = "예약 현황",
                        style = AppTextStyle.Body.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = IconTextColor)
                    )
                }
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
                    text = if (selectedTab == 0) "현재 예약 현황이 없습니다." else "방문 완료된 예약 건이 없습니다.",
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
                    OwnerReservedList(
                        reservation = item,
                        store = store
                    )
                }
            }
        }
    }
}