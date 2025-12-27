package com.example.searchplacement.presentation.user.favorite

sealed interface FavoriteAction {

    data class OnStoreClick(val storeId: Long) : FavoriteAction
    data class OnFavoriteToggle(val storeId: Long) : FavoriteAction
}
