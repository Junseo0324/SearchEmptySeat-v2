package com.example.searchplacement.presentation.user.category

sealed interface CategoryAction {
    data class OnCategorySelected(val category: String) : CategoryAction
    data class OnSortSelected(val displayName: String, val value: String) : CategoryAction
    data class OnStoreClick(val storeId: Long) : CategoryAction
    data object OnSortButtonClick : CategoryAction
    data object OnSortDismiss : CategoryAction
}
