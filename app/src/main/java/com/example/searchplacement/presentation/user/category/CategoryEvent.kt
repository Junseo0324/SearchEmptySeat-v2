package com.example.searchplacement.presentation.user.category

sealed interface CategoryEvent {
    data class NavigateToStoreDetail(val storeId: Long) : CategoryEvent
    data class ShowSnackbar(val message: String) : CategoryEvent
}
