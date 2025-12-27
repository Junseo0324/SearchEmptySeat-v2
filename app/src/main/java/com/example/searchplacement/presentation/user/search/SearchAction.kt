package com.example.searchplacement.presentation.user.search

sealed interface SearchAction {
    data class OnQueryChanged(val query: String) : SearchAction
    data object OnSearch : SearchAction
    data object OnClearQuery : SearchAction
    data class OnStoreClick(val storeId: Long) : SearchAction
}
