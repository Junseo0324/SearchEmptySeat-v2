package com.example.searchplacement.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector


sealed class MainBottomNavItem(
    var title: String, val icon: ImageVector, val screenRoute: String
) {
    data object Home : MainBottomNavItem("홈", Icons.Outlined.Home, "HOME")
    data object Category : MainBottomNavItem("카테고리", Icons.Outlined.Search, "CATEGORY")
    data object Reserve : MainBottomNavItem("예약", Icons.Outlined.Call, "RESERVE")
    data object Favorite : MainBottomNavItem("찜", Icons.Default.FavoriteBorder, "FAVORITE")
    data object Setting : MainBottomNavItem("설정", Icons.Outlined.Person, "SETTING")

    companion object {
        val items = listOf(Home, Category, Reserve, Favorite, Setting)
    }
}