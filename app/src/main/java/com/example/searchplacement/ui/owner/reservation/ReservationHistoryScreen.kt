package com.example.searchplacement.ui.owner.reservation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.viewmodel.ReservationViewModel
import com.example.searchplacement.viewmodel.StoreListViewModel
import kotlinx.coroutines.flow.map


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationHistoryScreen(navController: NavHostController) {
    val storeListViewModel: StoreListViewModel = hiltViewModel()
    val viewModel: ReservationViewModel = hiltViewModel()

    val completedReservations = viewModel.reservations.map {
            list -> list.filter { it.status == "completed" }
    }.collectAsState(initial = emptyList()).value

    LaunchedEffect(Unit) {
        val storeId = storeListViewModel.selectedStore.value?.storePK
        if (storeId != null) {
            viewModel.fetchOwnerReservations(storeId)
        }
    }

    //todo : AppBar가 조금 이상함..
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {Text("예약 내역")},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로 가기"
                        )
                    }
                },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(Dimens.Tiny)
        ) {
            Text("예약 완료 건(${completedReservations.size})", style = AppTextStyle.Caption, textAlign = TextAlign.End)
            if (completedReservations.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(completedReservations) {
                        CompletedReservationCard(it)
                    }
                }
            } else {

                Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                    Text(text = "예약 내역이 없습니다.")
                }
            }
        }


    }
}