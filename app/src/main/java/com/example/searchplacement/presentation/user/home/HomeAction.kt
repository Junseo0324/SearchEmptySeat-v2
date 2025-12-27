package com.example.searchplacement.presentation.user.home

sealed interface HomeAction {
    // LoadMapPins removed as per request
    data class OnMarkerClick(val storeId: Long) : HomeAction
    data class OnStoreDetailClick(val storeId: Long) : HomeAction
}
