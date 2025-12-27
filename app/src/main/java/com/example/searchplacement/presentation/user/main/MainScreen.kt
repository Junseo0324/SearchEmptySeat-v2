package com.example.searchplacement.presentation.user.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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
                containerColor = androidx.compose.ui.graphics.Color.White,
                contentColor = androidx.compose.ui.graphics.Color.Black,
                currentRoute = selectedRoute,
                onItemClick = onBottomNavSelected
            )
        }
    ) { innerPadding ->
        content(Modifier.padding(innerPadding))
    }
}
