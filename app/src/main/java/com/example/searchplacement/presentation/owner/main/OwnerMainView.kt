package com.example.searchplacement.presentation.owner.main

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.searchplacement.core.navigation.OwnerBottomNavItem
import com.example.searchplacement.core.navigation.OwnerNavigation
import com.example.searchplacement.presentation.theme.Black
import com.example.searchplacement.presentation.theme.White
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.searchplacement.presentation.owner.main.OwnerMainViewModel

@Composable
fun OwnerMainView() {
    val navController = rememberNavController()

    val ownerMainViewModel: OwnerMainViewModel = hiltViewModel()
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
        OwnerNavigation(navController,ownerMainViewModel)
    }
}
