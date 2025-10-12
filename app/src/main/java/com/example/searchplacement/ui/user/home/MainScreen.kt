package com.example.searchplacement.ui.user.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.user.map.NaverMapScreen
import com.example.searchplacement.viewmodel.MapViewModel

@Composable
fun MainScreen(navController: NavHostController) {

    val mapViewModel: MapViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        mapViewModel.loadMapPins()
    }
    Column {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart)
            ) {
                HeaderSection()

                Spacer(modifier = Modifier.height(Dimens.Medium))

                SearchBar(navController)

                Spacer(modifier = Modifier.height(Dimens.Medium))
                NaverMapScreen(
                    navController = navController,
                    mapViewModel
                )

            }
        }
    }
}