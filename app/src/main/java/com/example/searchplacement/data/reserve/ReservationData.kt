package com.example.searchplacement.data.reserve

import com.example.searchplacement.data.menu.MenuItemData
import java.time.LocalDate

data class ReservationData(
    var numberOfPeople: Int = 2,
    var selectedDate: LocalDate? = null,
    var selectedTime: String? = null,
    var selectedTable: String? = null,
    var selectedMenus: MutableMap<String, MenuItemData> = mutableMapOf(),
    var totalPrice: Int = 0,
    var paymentMethod: String = "offline"
)
