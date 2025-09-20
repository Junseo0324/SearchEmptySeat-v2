package com.example.searchplacement.ui.owner.placement

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.searchplacement.viewmodel.PlacementViewModel
import com.example.searchplacement.viewmodel.StoreListViewModel

@Composable
fun CheckPlacementScreen(
    navController: NavHostController,
    storeListViewModel: StoreListViewModel,
    placementViewModel: PlacementViewModel
) {
    val placementState by placementViewModel.placement.collectAsState()
    val selectedStore = storeListViewModel.selectedStore.collectAsState().value

    LaunchedEffect(Unit) {
        selectedStore?.let {
            placementViewModel.getPlacementByStore(it.storePK)
        }
    }
    // placementState?.data가 있으면 바로 자리 편집, 아니면 사이즈 선택
    LaunchedEffect(placementState) {
        val layoutSize = placementState?.data?.layoutSize
        if (layoutSize != null) {
            // 기존 자리 배치 존재 → 바로 자리편집
            navController.navigate("placement/$layoutSize") {
                popUpTo("checkPlacement") { inclusive = true }
            }
        } else {
            // 배치 정보 없음 → 사이즈 선택부터
            navController.navigate("store_size") {
                popUpTo("checkPlacement") { inclusive = true }
            }
        }
    }



    // Optionally: 로딩 표시 등
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("자리배치 정보를 확인 중입니다...")
    }
}
