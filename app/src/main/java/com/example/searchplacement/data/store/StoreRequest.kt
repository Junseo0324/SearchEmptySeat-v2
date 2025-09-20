package com.example.searchplacement.data.store

data class StoreRequest(
    val storeName: String,
    val location: String,
    val description: String,
    val businessRegistrationNumber: String,
    val bank: String,
    val accountNumber: String,
    val depositor: String,
    val businessHours: Map<String, String>,
    val category: List<String>,
    val regularHolidays: Map<String, Int>?,
    val temporaryHolidays: List<String>?,
)
