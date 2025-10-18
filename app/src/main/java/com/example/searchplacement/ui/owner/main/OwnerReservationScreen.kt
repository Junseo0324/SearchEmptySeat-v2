package com.example.searchplacement.ui.owner.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.searchplacement.ui.owner.reservation.CompletedReservationCard
import com.example.searchplacement.ui.owner.reservation.PendingReservationCard
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.user.reserve.my.ReservationTabBar
import com.example.searchplacement.viewmodel.ReservationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerReservationScreen(storeId: Long) {
    val viewModel: ReservationViewModel = hiltViewModel()
    val stateList = listOf("진행 중", "완료")

    var selectedIndex by remember { mutableStateOf(0) }
    val selectedState = stateList[selectedIndex]

    LaunchedEffect(Unit) {
        viewModel.fetchOwnerReservations(storeId)
    }
    val reservations by viewModel.reservations.collectAsState()


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {Text("예약 내역")}
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(Dimens.Tiny)
        ) {
            ReservationTabBar(
                selectedIndex = selectedIndex,
                onTabSelected = { selectedIndex = it },
                stateList = stateList
            )

            if (selectedState == "진행 중") {
                val reservationList = reservations.filter { it.status == "pending" }

                LazyColumn {
                    items(reservationList) { reservation ->
                        PendingReservationCard(reservation)
                    }
                }
            } else {
                val completedReservationList = reservations.filter { it.status == "completed" }
                LazyColumn {
                    items(completedReservationList) { reservation ->
                        CompletedReservationCard(reservation)
                    }
                }
            }
        }

    }
}