package com.example.searchplacement.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
import com.example.searchplacement.viewmodel.OwnerMainViewModel

@Composable
fun OwnerNavigation(navController: NavHostController, ownerMainViewModel: OwnerMainViewModel) {
    val storeId = ownerMainViewModel.selectedStoreId.collectAsState().value
    NavHost(navController = navController, startDestination = "storeSelect") {
        composable("storeSelect") { StoreSelectScreen(navController,ownerMainViewModel) }

        composable(OwnerBottomNavItem.Home.screenRoute) { backStackEntry ->
            OwnerHomeScreen(storeId = storeId?.toLong() ?: 0L)
        }
        composable(OwnerBottomNavItem.Store.screenRoute) {
            OwnerStoreScreen(navController,storeId?.toLong() ?: 0L)
        }
        composable(OwnerBottomNavItem.Reservation.screenRoute) {
            OwnerReservationScreen(storeId?.toLong() ?: 0L)
        }
        composable("store_size") {
            StoreSizeSelectionScreen(
                onNext = { selectedSize ->
                    navController.navigate("placement/$selectedSize") {
                        popUpTo("store_size") { inclusive = true }
                    }
                }
            )
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