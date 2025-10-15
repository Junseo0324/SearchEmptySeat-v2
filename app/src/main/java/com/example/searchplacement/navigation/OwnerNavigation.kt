package com.example.searchplacement.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.searchplacement.ui.owner.info.BusinessHourScreen
import com.example.searchplacement.ui.owner.info.RegisterStore
import com.example.searchplacement.ui.owner.info.StoreInformationScreen
import com.example.searchplacement.ui.owner.main.OwnerHomeScreen
import com.example.searchplacement.ui.owner.main.OwnerReservationScreen
import com.example.searchplacement.ui.owner.main.OwnerStoreScreen
import com.example.searchplacement.ui.owner.main.StoreSelectScreen
import com.example.searchplacement.ui.owner.menu_manage.EditMenuScreen
import com.example.searchplacement.ui.owner.menu_manage.MenuInformationScreen
import com.example.searchplacement.ui.owner.placement.CheckPlacementScreen
import com.example.searchplacement.ui.owner.placement.EditPlacementScreen
import com.example.searchplacement.ui.owner.placement.StoreSizeSelectionScreen
import com.example.searchplacement.ui.owner.placement.TableEditorScreen
import com.example.searchplacement.ui.owner.reservation.ReservationHistoryScreen
import com.example.searchplacement.ui.owner.review.OwnerReviewScreen
import com.example.searchplacement.ui.owner.section.EditSectionScreen
import com.example.searchplacement.viewmodel.MainViewModel
import com.example.searchplacement.viewmodel.MenuSectionViewModel
import com.example.searchplacement.viewmodel.MenuViewModel
import com.example.searchplacement.viewmodel.OwnerStoreViewModel
import com.example.searchplacement.viewmodel.PlacementViewModel
import com.example.searchplacement.viewmodel.StoreListViewModel

@Composable
fun OwnerNavigation(
    navController: NavHostController = rememberNavController(),
    storeListViewModel: StoreListViewModel,
    mainViewModel: MainViewModel,
    ownerStoreViewModel: OwnerStoreViewModel,
    placementViewModel: PlacementViewModel,
    menuSectionViewModel: MenuSectionViewModel,
    menuViewModel: MenuViewModel
) {
    NavHost(navController = navController, startDestination = "storeSelect") {
        composable(OwnerBottomNavItem.Home.screenRoute) {
            OwnerHomeScreen()
        }
        composable(OwnerBottomNavItem.Store.screenRoute) {
            OwnerStoreScreen(navController)
        }
        composable(OwnerBottomNavItem.Reservation.screenRoute) {
            OwnerReservationScreen()
        }
        composable("storeSelect") {
            StoreSelectScreen(
                onStoreSelected = {
                    navController.navigate(OwnerBottomNavItem.Home.screenRoute)
                }
            )
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
            RegisterStore(
                navController = navController
            )
        }

        composable("checkPlacement") {
            CheckPlacementScreen(
                navController = navController
            )
        }

        composable("place_edit") {
            EditPlacementScreen(
                navController = navController
            )
        }
        composable("placement/{layoutSize}") { backStackEntry ->
            val layoutSize = backStackEntry.arguments?.getString("layoutSize")?.toIntOrNull() ?: 1
            TableEditorScreen(
                layoutSize = layoutSize,
                placementViewModel = placementViewModel,
                storeListViewModel = storeListViewModel
            )
        }

        composable("editSection") {
            EditSectionScreen(
                navController = navController,
                menuSectionViewModel = menuSectionViewModel,
                storeListViewModel = storeListViewModel
            )
        }

        composable("editMenu") {
            MenuInformationScreen(storeListViewModel,menuViewModel,menuSectionViewModel,navController)
        }
        composable("soldOut") {
            EditMenuScreen(storeListViewModel,menuViewModel,menuSectionViewModel)
        }

        composable("reservationHistory") {
            ReservationHistoryScreen(navController,storeListViewModel)
        }

        composable("storeReview") {
            OwnerReviewScreen(navController,storeListViewModel)
        }

    }
}