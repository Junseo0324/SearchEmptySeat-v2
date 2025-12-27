package com.example.searchplacement.presentation.user.favorite

sealed interface FavoriteEvent {
    data class NavigateToStoreDetail(val storeId: Long) : FavoriteEvent
    data class ShowSnackbar(val message: String) : FavoriteEvent
}
