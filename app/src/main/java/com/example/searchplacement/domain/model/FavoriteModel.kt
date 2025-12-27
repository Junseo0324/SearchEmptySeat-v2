package com.example.searchplacement.domain.model

data class FavoriteModel(
    val favoritePK: Long,
    val userId: Long,
    val store: StoreModel,
    val createdDate: String
)
