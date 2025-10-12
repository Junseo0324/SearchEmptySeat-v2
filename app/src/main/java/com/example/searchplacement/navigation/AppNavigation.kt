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
import com.example.searchplacement.ui.user.reserve.store.ReservationFlowScreen
import com.example.searchplacement.ui.user.search.SearchScreen
import com.example.searchplacement.ui.user.setting.InformationScreen
import com.example.searchplacement.ui.user.setting.SettingScreen
import com.example.searchplacement.ui.user.store.StoreMapScreen
import com.example.searchplacement.ui.user.store.StoreScreen
import com.example.searchplacement.viewmodel.LoginViewModel
import com.example.searchplacement.viewmodel.MainViewModel

@Composable
fun LoginNavigation(navController: NavHostController,loginViewModel: LoginViewModel) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController)
        }
        composable("register") {
            RegisterScreen(navController)
        }
        composable("find_password") { FindPasswordScreen(navController, loginViewModel) }

    }
}

@Composable
fun MainNavigation(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {

    NavHost(navController = navController, startDestination = "main") {
        navigation(
            startDestination = MainBottomNavItem.Home.screenRoute,
            route = "main"
        ) {
            composable(MainBottomNavItem.Home.screenRoute) { MainScreen(navController) }
            composable(MainBottomNavItem.Category.screenRoute) { CategoryScreen(navController) }
            composable(MainBottomNavItem.Reserve.screenRoute) { ReserveScreen(navController) }
            composable(MainBottomNavItem.Favorite.screenRoute) { FavoriteScreen(navController) }
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
            composable("search") { SearchScreen(navController) }

            composable(
                route = "store/{storeId}",
                arguments = listOf(navArgument("storeId") { type = NavType.LongType })
            ) { backStackEntry ->
                val storeId = backStackEntry.arguments?.getLong("storeId") ?: 0L
                StoreScreen(navController, storeId)
            }

            composable("map_with_store") {
                val store = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<StoreResponse>("store") ?: return@composable

                StoreMapScreen(navController, store)
            }
            composable(
                route = "reservation_store/{storeId}",
                arguments = listOf(navArgument("storeId") { type = NavType.LongType })
            ){ backStackEntry ->
                val storeId = backStackEntry.arguments?.getLong("storeId") ?: 0L
                ReservationFlowScreen(navController,storeId)
            }
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
        }


    }
}

