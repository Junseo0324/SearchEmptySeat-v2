package com.example.searchplacement.data.menu

data class MenuItemData(
    val menuId: String,
    val name: String,
    val price: Int,
    var quantity: Int = 0
)