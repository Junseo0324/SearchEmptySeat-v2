package com.example.searchplacement.data.store

data class FavoriteResponse(
    val favoritePK: Long,
    val userId: String?,
    val store: Store,
    val createdDate: String
)
