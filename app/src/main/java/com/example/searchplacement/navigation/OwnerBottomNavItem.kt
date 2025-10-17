package com.example.searchplacement.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Store
import androidx.compose.ui.graphics.vector.ImageVector

sealed class OwnerBottomNavItem(
    val title: String,
    val outlinedIcon: ImageVector,
    val filledIcon: ImageVector,
    val screenRoute: String
) {
    object Home : OwnerBottomNavItem("홈", Icons.Outlined.Home, Icons.Filled.Home, "owner_home/{storeId}")
    object Store : OwnerBottomNavItem("매장 관리", Icons.Outlined.Store, Icons.Filled.Store, "owner_store")
    object Reservation : OwnerBottomNavItem("예약 관리", Icons.Outlined.Event, Icons.Filled.Event, "owner_reserve")
}