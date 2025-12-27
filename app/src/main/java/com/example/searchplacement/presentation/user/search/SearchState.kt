package com.example.searchplacement.presentation.user.search

import com.example.searchplacement.domain.model.StoreModel

data class SearchState(
    val query: String = "",
    val searchResults: List<StoreModel>? = null,
    val isLoading: Boolean = false
)
