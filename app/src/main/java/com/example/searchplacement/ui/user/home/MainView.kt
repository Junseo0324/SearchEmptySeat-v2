package com.example.searchplacement.ui.user.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.searchplacement.navigation.MainBottomNavItem
import com.example.searchplacement.navigation.MainNavigation
import com.example.searchplacement.viewmodel.FavoriteViewModel
import com.example.searchplacement.viewmodel.MainViewModel
import com.example.searchplacement.viewmodel.MenuSectionViewModel
import com.example.searchplacement.viewmodel.MenuViewModel
import com.example.searchplacement.viewmodel.PlacementViewModel
import com.example.searchplacement.viewmodel.StoreViewModel


@Composable
fun MainView() {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = hiltViewModel()
    val storeViewModel: StoreViewModel = hiltViewModel()
    val favoriteViewModel: FavoriteViewModel = hiltViewModel()
    val menuViewModel: MenuViewModel = hiltViewModel()
    val menuSectionViewModel: MenuSectionViewModel = hiltViewModel()
    val placementViewModel: PlacementViewModel = hiltViewModel()
    Scaffold(
        modifier = Modifier.background(Color.White),
        topBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val items = MainBottomNavItem.items.map { it.screenRoute }

            if (items.contains(currentRoute)) {
                val currentItem = MainBottomNavItem.items.find { it.screenRoute == currentRoute }
                MainTopBar(title = currentItem?.title ?: "")
            }
        },
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
                .background(Color.White)
        ) {
            MainNavigation(navController, mainViewModel, storeViewModel, favoriteViewModel,menuViewModel,menuSectionViewModel,placementViewModel)
        }
    }
}