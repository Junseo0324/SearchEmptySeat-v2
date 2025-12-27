package com.example.searchplacement.data.mapper

import com.example.searchplacement.data.store.StoreResponse
import com.example.searchplacement.domain.model.StoreModel

fun StoreResponse.toModel(): StoreModel {
    return StoreModel(
        storePK = storePK,
        storeName = storeName,
        location = location,
        description = description,
        businessRegistrationNumber = businessRegistrationNumber,
        bank = bank,
        accountNumber = accountNumber,
        depositor = depositor,
        businessHours = businessHours,
        image = image,
        category = category,
        viewCount = viewCount,
        averageRating = averageRating,
        favoriteCount = favoriteCount,
        regularHolidays = regularHolidays,
        temporaryHolidays = temporaryHolidays
    )
}
