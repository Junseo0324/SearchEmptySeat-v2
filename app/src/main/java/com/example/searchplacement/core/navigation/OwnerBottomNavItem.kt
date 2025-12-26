package com.example.searchplacement.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.TableRestaurant
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material.icons.outlined.TableRestaurant
import androidx.compose.ui.graphics.vector.ImageVector

sealed class OwnerBottomNavItem(
    val title: String,
    val outlinedIcon: ImageVector,
    val filledIcon: ImageVector,
    val screenRoute: String
) {
    object Home : OwnerBottomNavItem("매장 관리", Icons.Outlined.Store, Icons.Filled.Store, "owner_home")
    object Store : OwnerBottomNavItem("메뉴 관리", Icons.Outlined.Restaurant, Icons.Filled.Restaurant, "owner_store")
    object Reservation : OwnerBottomNavItem("예약 관리", Icons.Outlined.TableRestaurant, Icons.Filled.TableRestaurant, "owner_reserve")
}