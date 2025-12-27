package com.example.searchplacement.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import com.example.searchplacement.presentation.user.favorite.FavoriteScreenRoot
import com.example.searchplacement.presentation.user.home.HomeScreenRoot
import com.example.searchplacement.presentation.user.password.check.CheckPasswordScreenRoot
import com.example.searchplacement.presentation.user.password.update.UpdatePasswordScreenRoot
import com.example.searchplacement.presentation.user.reservation.ReserveScreen
import com.example.searchplacement.presentation.user.reservationstore.ReservationFlowScreen
import com.example.searchplacement.presentation.user.search.SearchScreenRoot
import com.example.searchplacement.presentation.user.information.InformationScreenRoot
import com.example.searchplacement.presentation.user.setting.SettingScreenRoot
import com.example.searchplacement.presentation.user.store.StoreMapScreen
import com.example.searchplacement.presentation.user.store.StoreScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
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

        composable(MainBottomNavItem.Home.screenRoute) { 
            HomeScreenRoot(
                onNavigateToStoreDetail = { storeId ->
                    navController.navigate("store/$storeId")
                },
                onNavigateToSearch = {
                    navController.navigate("search")
                }
            )
        }
        composable(MainBottomNavItem.Category.screenRoute) { 
            CategoryScreenRoot(
                onNavigateToStoreDetail = { storeId ->
                    navController.navigate("store/$storeId")
                }
            ) 
        }
        composable(MainBottomNavItem.Reserve.screenRoute) { ReserveScreen(navController) }
        composable(MainBottomNavItem.Favorite.screenRoute) { 
            FavoriteScreenRoot(
                onNavigateToStoreDetail = { storeId ->
                    navController.navigate("store/$storeId")
                }
            )
        }
        composable(MainBottomNavItem.Setting.screenRoute) { 
            SettingScreenRoot(
                onNavigateToInformation = {
                    navController.navigate("information")
                },
                onNavigateToCheckPassword = {
                    navController.navigate("checkPassword")
                },
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("information") { 
            InformationScreenRoot(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("search") {
            SearchScreenRoot(
                onNavigateToStore = { storeId ->
                    navController.navigate("store/$storeId")
                }
            )
        }

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
            CheckPasswordScreenRoot(
                onNavigateToUpdatePassword = {
                    navController.navigate("updatePassword")
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("updatePassword") {
            UpdatePasswordScreenRoot(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSuccessUpdate = {
                    navController.popBackStack(
                        route = MainBottomNavItem.Setting.screenRoute,
                        inclusive = false
                    )
                }
            )
        }
        composable("owner_main") {
            OwnerMainView()
        }
    }
}
