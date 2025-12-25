package com.example.searchplacement.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.searchplacement.data.store.StoreResponse
import com.example.searchplacement.presentation.owner.main.OwnerMainView
import com.example.searchplacement.presentation.user.category.CategoryScreen
import com.example.searchplacement.presentation.user.favorite.FavoriteScreen
import com.example.searchplacement.presentation.user.home.MainScreen
import com.example.searchplacement.presentation.user.login.CheckPassword
import com.example.searchplacement.presentation.user.login.FindPasswordScreen
import com.example.searchplacement.presentation.user.login.LoginScreen
import com.example.searchplacement.presentation.user.login.RegisterScreen
import com.example.searchplacement.presentation.user.login.UpdatePassword
import com.example.searchplacement.presentation.user.reserve.my.ReserveScreen
import com.example.searchplacement.presentation.user.reserve.store.ReservationFlowScreen
import com.example.searchplacement.presentation.user.search.SearchScreen
import com.example.searchplacement.presentation.user.setting.InformationScreen
import com.example.searchplacement.presentation.user.setting.SettingScreen
import com.example.searchplacement.presentation.user.store.StoreMapScreen
import com.example.searchplacement.presentation.user.store.StoreScreen
import com.example.searchplacement.presentation.user.login.LoginViewModel
import com.example.searchplacement.presentation.user.main.MainViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val sharedMainViewModel: MainViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {
        composable("login") {
            LoginScreen(navController)
        }
        composable("register") {
            RegisterScreen(navController)
        }
        composable("find_password") {
            FindPasswordScreen(navController, loginViewModel)
        }

        composable(MainBottomNavItem.Home.screenRoute) { MainScreen(navController) }
        composable(MainBottomNavItem.Category.screenRoute) { CategoryScreen(navController) }
        composable(MainBottomNavItem.Reserve.screenRoute) { ReserveScreen(navController) }
        composable(MainBottomNavItem.Favorite.screenRoute) { FavoriteScreen(navController) }
        composable(MainBottomNavItem.Setting.screenRoute) { SettingScreen(navController, sharedMainViewModel) }
        composable("information") { InformationScreen(navController, sharedMainViewModel) }
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
        ) { backStackEntry ->
            val storeId = backStackEntry.arguments?.getLong("storeId") ?: 0L
            ReservationFlowScreen(navController, storeId)
        }
        composable("checkPassword") {
            CheckPassword(
                navController = navController
            )
        }
        composable("updatePassword") {
            UpdatePassword(
                navController = navController
            )
        }
        composable("owner_main") {
            OwnerMainView()
        }
    }
}
