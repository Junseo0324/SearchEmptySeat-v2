package com.example.searchplacement.presentation.user.home

sealed interface HomeEvent {
    data class ShowSnackbar(val message: String) : HomeEvent
    data class NavigateToStoreDetail(val storeId: Long) : HomeEvent
}
