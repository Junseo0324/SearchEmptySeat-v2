package com.example.searchplacement.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.searchplacement.ui.owner.info.BusinessHourScreen
import com.example.searchplacement.ui.owner.info.RegisterStore
import com.example.searchplacement.ui.owner.info.StoreInformationScreen
import com.example.searchplacement.ui.owner.main.OwnerHomeScreen
import com.example.searchplacement.ui.owner.main.OwnerReservationScreen
import com.example.searchplacement.ui.owner.main.OwnerStoreScreen
import com.example.searchplacement.ui.owner.menu_manage.EditMenuScreen
import com.example.searchplacement.ui.owner.menu_manage.MenuInformationScreen
import com.example.searchplacement.ui.owner.placement.CheckPlacementScreen
import com.example.searchplacement.ui.owner.placement.EditPlacementScreen
import com.example.searchplacement.ui.owner.placement.StoreSizeSelectionScreen
import com.example.searchplacement.ui.owner.placement.TableEditorScreen
import com.example.searchplacement.ui.owner.reservation.ReservationHistoryScreen
import com.example.searchplacement.ui.owner.review.OwnerReviewScreen
import com.example.searchplacement.ui.owner.section.EditSectionScreen
import com.example.searchplacement.ui.owner.selection.StoreSelectScreen

@Composable
fun OwnerNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "storeSelect") {
        composable("storeSelect") { StoreSelectScreen(navController) }

        navigation(startDestination = OwnerBottomNavItem.Home.screenRoute, route = "owner_main/{storeId}") {
            composable(OwnerBottomNavItem.Home.screenRoute) { backStackEntry ->
                val storeId = backStackEntry.arguments?.getString("storeId")?.toIntOrNull()
                OwnerHomeScreen(storeId = storeId?.toLong() ?: 0L)
            }
            composable(OwnerBottomNavItem.Store.screenRoute) {
                OwnerStoreScreen(navController)
            }
            composable(OwnerBottomNavItem.Reservation.screenRoute) {
                OwnerReservationScreen()
            }
        }
        composable("store_size") {
            StoreSizeSelectionScreen(onNext = { selectedSize ->
                navController.navigate("placement/$selectedSize") {
                    popUpTo("store_size") { inclusive = true }
                }
            })
        }

        composable("storeInfo") {
            StoreInformationScreen(navController)
        }
        composable("businessHour") {
            BusinessHourScreen(navController)
        }
        composable("storeRegister") {
            RegisterStore(navController)
        }

        composable("checkPlacement") {
            CheckPlacementScreen(navController)
        }

        composable("place_edit") {
            EditPlacementScreen(navController)
        }
        composable("placement/{layoutSize}") { backStackEntry ->
            val layoutSize = backStackEntry.arguments?.getString("layoutSize")?.toIntOrNull() ?: 1
            TableEditorScreen(layoutSize)
        }

        composable("editSection") {
            EditSectionScreen(navController)
        }

        composable("editMenu") {
            MenuInformationScreen()
        }
        composable("soldOut") {
            EditMenuScreen()
        }

        composable("reservationHistory") {
            ReservationHistoryScreen(navController)
        }

        composable("storeReview") {
            OwnerReviewScreen(navController)
        }

    }
}