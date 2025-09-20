package com.example.searchplacement.data.menu

data class OutOfStockRequest(
    val menus: List<MenuStockDto>
)

data class MenuStockDto(
    val menuPK: Long,
    val available: Boolean
)
