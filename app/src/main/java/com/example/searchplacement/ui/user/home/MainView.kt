package com.example.searchplacement.ui.user.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.searchplacement.navigation.MainNavigation
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.viewmodel.MainViewModel


@Composable
fun MainView() {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = hiltViewModel()
    Scaffold(
        modifier = Modifier.background(White),
        bottomBar = {
            MainBottomBar(
                Color.White, Color.Black,
                navController
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .background(White)
        ) {
            MainNavigation(navController, mainViewModel)
        }
    }
}