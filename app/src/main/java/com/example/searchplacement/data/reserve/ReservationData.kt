package com.example.searchplacement.data.reserve

import java.time.LocalDate

data class ReservationData(
    var numberOfPeople: Int = 2,
    var selectedDate: LocalDate? = null,
    var selectedTime: String? = null,
    var selectedTable: String? = null,
    var selectedMenus: MutableMap<Long, Int> = mutableMapOf(),
    var totalPrice: Int = 0
)