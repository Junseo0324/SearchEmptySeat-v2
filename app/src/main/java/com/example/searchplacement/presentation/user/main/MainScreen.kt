package com.example.searchplacement.presentation.user.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import com.example.searchplacement.presentation.user.home.MainBottomBar

@Composable
fun MainScreen(
    selectedRoute: String?,
    onBottomNavSelected: (String) -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        bottomBar = {
            MainBottomBar(
                containerColor = Color.White,
                contentColor = Color.Black,
                currentRoute = selectedRoute,
                onItemClick = onBottomNavSelected
            )
        }
    ) { innerPadding ->
        content(Modifier.padding(innerPadding))
    }
}
