package com.example.searchplacement.navigation

import com.example.searchplacement.R

sealed class OwnerBottomNavItem(var title: String, val icon: Int, val screenRoute: String) {
    object Home : OwnerBottomNavItem("홈", R.drawable.ic_home,"owner_home")
    object Store : OwnerBottomNavItem("매장 관리", R.drawable.ic_store, "owner_store")
    object Reservation : OwnerBottomNavItem("예약 관리", R.drawable.ic_reserve,"owner_reserve")
}