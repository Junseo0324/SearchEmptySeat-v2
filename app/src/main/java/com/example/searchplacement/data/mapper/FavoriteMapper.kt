package com.example.searchplacement.data.mapper

import com.example.searchplacement.data.store.FavoriteResponse
import com.example.searchplacement.data.store.Store
import com.example.searchplacement.domain.model.FavoriteModel
import com.example.searchplacement.domain.model.StoreModel

fun FavoriteResponse.toModel(): FavoriteModel {
    return FavoriteModel(
        favoritePK = favoritePK,
        userId = userId ?: "",
        store = store.toModel(),
        createdDate = createdDate
    )
}

fun Store.toModel(): StoreModel {
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
        regularHolidays = null,
        temporaryHolidays = null
    )
}
