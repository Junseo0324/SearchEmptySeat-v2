package com.example.searchplacement.ui.owner.main

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.searchplacement.navigation.OwnerBottomNavItem
import com.example.searchplacement.navigation.OwnerNavigation
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.White
import androidx.compose.runtime.getValue

@Composable
fun OwnerMainView() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavRoutes = listOf(
        OwnerBottomNavItem.Home.screenRoute,
        OwnerBottomNavItem.Store.screenRoute,
        OwnerBottomNavItem.Reservation.screenRoute
    )

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomNavRoutes) {
                OwnerBottomNavigation(
                    containerColor = White,
                    contentColor = Black,
                    navController = navController
                )
            }
        }
    ) { padding ->
        OwnerNavigation(navController)
    }
}
