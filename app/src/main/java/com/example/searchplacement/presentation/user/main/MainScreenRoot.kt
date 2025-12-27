package com.example.searchplacement.presentation.user.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.searchplacement.core.navigation.AppNavigation

@Composable
fun MainScreenRoot() {
    val navController = rememberNavController()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    MainScreen(
        selectedRoute = currentRoute,
        onBottomNavSelected = { route ->
            navController.navigate(route) {
                navController.graph.startDestinationRoute?.let {
                    popUpTo(it) {
                        saveState = true
                    }
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    ) { modifier ->
        AppNavigation(
            navController = navController,
            modifier = modifier
        )
    }
}
