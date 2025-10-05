package com.example.searchplacement.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.searchplacement.data.reserve.ReservationDraft
import com.example.searchplacement.data.store.StoreResponse
import com.example.searchplacement.ui.user.category.CategoryScreen
import com.example.searchplacement.ui.user.favorite.FavoriteScreen
import com.example.searchplacement.ui.user.home.MainScreen
import com.example.searchplacement.ui.user.login.CheckPassword
import com.example.searchplacement.ui.user.login.FindPasswordScreen
import com.example.searchplacement.ui.user.login.LoginScreen
import com.example.searchplacement.ui.user.login.RegisterScreen
import com.example.searchplacement.ui.user.login.UpdatePassword
import com.example.searchplacement.ui.user.reserve.my.ReserveScreen
import com.example.searchplacement.ui.user.reserve.store.SeatMenuSelectionScreen
import com.example.searchplacement.ui.user.reserve.store.StoreReservationScreen
import com.example.searchplacement.ui.user.search.SearchScreen
import com.example.searchplacement.ui.user.setting.InformationScreen
import com.example.searchplacement.ui.user.setting.SettingScreen
import com.example.searchplacement.ui.user.store.StoreMapScreen
import com.example.searchplacement.ui.user.store.StoreScreen
import com.example.searchplacement.viewmodel.FavoriteViewModel
import com.example.searchplacement.viewmodel.LoginViewModel
import com.example.searchplacement.viewmodel.MainViewModel
import com.example.searchplacement.viewmodel.MenuSectionViewModel
import com.example.searchplacement.viewmodel.MenuViewModel
import com.example.searchplacement.viewmodel.PlacementViewModel
import com.example.searchplacement.viewmodel.StoreViewModel

@Composable
fun LoginNavigation(navController: NavHostController,loginViewModel: LoginViewModel) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController, loginViewModel)
        }
        composable("register") {
            RegisterScreen(navController, loginViewModel)
        }
        composable("find_password") { FindPasswordScreen(navController, loginViewModel) }

    }
}

@Composable
fun MainNavigation(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    storeViewModel: StoreViewModel,
    favoriteViewModel: FavoriteViewModel,
    menuViewModel: MenuViewModel,
    menuSectionViewModel: MenuSectionViewModel,
    placementViewModel: PlacementViewModel
) {

    NavHost(navController = navController, startDestination = "main") {
        navigation(
            startDestination = MainBottomNavItem.Home.screenRoute,
            route = "main"
        ) {
            composable(MainBottomNavItem.Home.screenRoute) { MainScreen(navController) }
            composable(MainBottomNavItem.Category.screenRoute) {
                CategoryScreen(
                    navController
                )
            }
            composable(MainBottomNavItem.Reserve.screenRoute) { ReserveScreen(navController) }
            composable(MainBottomNavItem.Favorite.screenRoute) {
                FavoriteScreen(
                    navController,
                    favoriteViewModel
                )
            }
            composable(MainBottomNavItem.Setting.screenRoute) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("main")
                }
                val sharedMainViewModel: MainViewModel = hiltViewModel(parentEntry)
                SettingScreen(navController, sharedMainViewModel)
            }
            composable("information") { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry("main")
                }
                val sharedMainViewModel: MainViewModel = hiltViewModel(parentEntry)
                InformationScreen(navController, sharedMainViewModel)
            }
            composable("search") { SearchScreen(navController, storeViewModel) }

            composable(
                route = "store/{storeId}",
                arguments = listOf(navArgument("storeId") { type = NavType.LongType })
            ) { backStackEntry ->
                val storeId = backStackEntry.arguments?.getLong("storeId") ?: 0L
                StoreScreen(
                    navController,
                    storeId,
                    storeViewModel,
                    mainViewModel,
                    favoriteViewModel,
                    menuViewModel,
                    menuSectionViewModel,
                    placementViewModel
                )
            }

            composable("map_with_store") {
                val store = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<StoreResponse>("store") ?: return@composable

                StoreMapScreen(navController, store)
            }
            composable("reserveStore") { StoreReservationScreen(navController, storeViewModel) }
            composable("checkPassword") {
                CheckPassword(
                    navController = navController,
                    mainViewModel = mainViewModel
                )
            }
            composable("updatePassword") {
                UpdatePassword(
                    navController = navController,
                    mainViewModel = mainViewModel
                )
            }
            composable("seatMenuSelection") {
                val reservationDraft = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<ReservationDraft>("reservationDraft") ?: return@composable

                SeatMenuSelectionScreen(
                    navController = navController,
                    placementViewModel = placementViewModel,
                    reservationDraft = reservationDraft,
                    menuViewModel = menuViewModel,
                    sectionViewModel = menuSectionViewModel
                )
            }
        }


    }
}

