package com.example.searchplacement.ui.user.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.user.map.NaverMapScreen

@Composable
fun MainScreen(navController: NavHostController) {

    val mapViewModel: MapViewModel = hiltViewModel()


    LaunchedEffect(Unit) {
        mapViewModel.loadMapPins()
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(Dimens.Small)
    ) {
        SearchBar(navController)
        Spacer(modifier = Modifier.height(16.dp))

        //NaverMap
        NaverMapScreen(navController,mapViewModel)
    }


}