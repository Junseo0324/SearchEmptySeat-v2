package com.example.searchplacement.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.searchplacement.presentation.owner.main.OwnerMainView
import com.example.searchplacement.presentation.user.auth.findpassword.FindPasswordScreenRoot
import com.example.searchplacement.presentation.user.auth.login.LoginScreenRoot
import com.example.searchplacement.presentation.user.auth.register.RegisterScreenRoot
import com.example.searchplacement.presentation.user.category.CategoryScreenRoot
import com.example.searchplacement.presentation.user.favorite.FavoriteScreen
import com.example.searchplacement.presentation.user.home.MainScreen
import com.example.searchplacement.presentation.user.login.CheckPassword
import com.example.searchplacement.presentation.user.login.UpdatePassword
import com.example.searchplacement.presentation.user.main.MainViewModel
import com.example.searchplacement.presentation.user.reserve.my.ReserveScreen
import com.example.searchplacement.presentation.user.reserve.store.ReservationFlowScreen
import com.example.searchplacement.presentation.user.search.SearchScreen
import com.example.searchplacement.presentation.user.setting.InformationScreen
import com.example.searchplacement.presentation.user.setting.SettingScreen
import com.example.searchplacement.presentation.user.store.StoreMapScreen
import com.example.searchplacement.presentation.user.store.StoreScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val sharedMainViewModel: MainViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {
        composable("login") {
            LoginScreenRoot(
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onNavigateToFindPassword = {
                    navController.navigate("find_password")
                },
                onNavigateToHome = { userType ->
                    if (userType == "OWNER") {
                        navController.navigate("owner_main") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            )
        }
        composable("register") {
            RegisterScreenRoot(
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("find_password") {
            FindPasswordScreenRoot(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(MainBottomNavItem.Home.screenRoute) { MainScreen(navController) }
        composable(MainBottomNavItem.Category.screenRoute) { 
            CategoryScreenRoot(
                onNavigateToStoreDetail = { storeId ->
                    navController.navigate("store/$storeId")
                }
            ) 
        }
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

        composable(
            route = "map_with_store/{storeId}",
            arguments = listOf(navArgument("storeId") { type = NavType.LongType })
        ) { backStackEntry ->
            val storeId = backStackEntry.arguments?.getLong("storeId") ?: 0L
            StoreMapScreen(navController, storeId)
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
