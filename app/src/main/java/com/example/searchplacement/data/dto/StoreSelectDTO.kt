package com.example.searchplacement.data.dto

data class StoreSelectDTO(
    val storePK: Long,
    val storeName: String,
    val location: String,
    val description: String,
    val businessRegistrationNumber: String,
    val bank: String,
    val accountNumber: String,
    val depositor: String,
    val businessHours: Map<String, String>,
    val image: List<String>,
    val category: List<String>,
    val viewCount: Int,
    val averageRating: Double,
    val favoriteCount: Int,
    val regularHolidays: Map<String, Int>?,
    val temporaryHolidays: List<String>?,
    val isAvailable: Boolean
)