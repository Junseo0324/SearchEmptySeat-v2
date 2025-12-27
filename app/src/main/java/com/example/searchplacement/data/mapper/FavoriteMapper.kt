package com.example.searchplacement.data.mapper

import com.example.searchplacement.data.store.FavoriteResponse
import com.example.searchplacement.data.store.Store
import com.example.searchplacement.domain.model.FavoriteModel
import com.example.searchplacement.domain.model.StoreModel

fun FavoriteResponse.toModel(): FavoriteModel {
    return FavoriteModel(
        favoritePK = favoritePK,
        userId = userId,
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
        // Store DTO might not have holidays, or they might be missing in Store.kt but present in StoreResponse.
        // Assuming Store DTO matches StoreModel except maybe nullability or specific fields.
        // Based on previous file read of Store.kt, it doesn't have holiday fields.
        // StoreModel has holiday fields as nullable. We can set them to null or modify StoreModel/Mapper logic if needed.
        // Let's set them to null for now as Store DTO doesn't seem to have them in the file I read.
        regularHolidays = null,
        temporaryHolidays = null
    )
}
