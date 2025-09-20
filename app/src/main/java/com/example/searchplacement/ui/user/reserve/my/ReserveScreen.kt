package com.example.searchplacement.ui.user.reserve.my

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.searchplacement.viewmodel.ReservationViewModel

@Composable
fun ReserveScreen(navController: NavHostController) {
    val stateList = listOf("방문 완료", "예약 중")
    var selectedIndex by remember { mutableStateOf(0) }
    val selectedState = stateList[selectedIndex]

    val reservationViewModel: ReservationViewModel = hiltViewModel()
    val reservations by reservationViewModel.reservations.collectAsState()

    LaunchedEffect(Unit) {
        reservationViewModel.getUserIdAndFetchReservations()
    }

    Column(
        Modifier
            .fillMaxSize()
    ) {
        ReservationTabBar(
            selectedIndex = selectedIndex,
            onTabSelected = { selectedIndex = it },
            stateList = stateList
        )

        if (selectedState == "방문 완료") {
            val confirmedList = reservations.filter { it.status == "completed" }
            LazyColumn {
                items(confirmedList.size) { index ->
                    ReservedList(navController, confirmedList[index])
                }
            }
        } else {
            val pending = reservations.firstOrNull { it.status == "pending" }
            if (pending != null) {
                ReservingStore(pending)
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = buildAnnotatedString {
//                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("현재 예약 중인 가게가\n")
//                            }
                            append("없습니다.")
                        },
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }


    }
}

